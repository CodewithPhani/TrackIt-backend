package com.skylinetransit.service;

import com.skylinetransit.dto.LocationUpdateRequest;
import com.skylinetransit.dto.NearbyResultDto;
import com.skylinetransit.dto.NearbyResultDto.NearbyBusDto;
import com.skylinetransit.dto.NearbyResultDto.NearbyStopDto;
import com.skylinetransit.model.Bus;
import com.skylinetransit.model.BusRoute;
import com.skylinetransit.model.BusStop;
import com.skylinetransit.model.UserLocation;
import com.skylinetransit.repository.BusRepository;
import com.skylinetransit.repository.BusStopRepository;
import com.skylinetransit.repository.RouteRepository;
import com.skylinetransit.repository.UserLocationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private static final double WALK_SPEED_KMH = 5.0; // average walking speed
    private static final double MAX_NEARBY_RADIUS_KM = 5.0; // 5 km radius

    @Value("${location.api.key:c2d1ebec4d3af3}")
    private String locationApiKey;

    private final UserLocationRepository userLocationRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final BusStopRepository busStopRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public LocationService(UserLocationRepository userLocationRepository,
                           BusRepository busRepository,
                           RouteRepository routeRepository,
                           BusStopRepository busStopRepository,
                           SimpMessagingTemplate messagingTemplate,
                           ObjectMapper objectMapper) {
        this.userLocationRepository = userLocationRepository;
        this.busRepository = busRepository;
        this.routeRepository = routeRepository;
        this.busStopRepository = busStopRepository;
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public NearbyResultDto updateLocationAndFindNearby(LocationUpdateRequest request) {
        // Persist current user location
        UserLocation location = new UserLocation(
                request.getSessionId(),
                request.getLatitude(),
                request.getLongitude(),
                request.getAccuracy(),
                request.getHeading(),
                request.getSpeed(),
                LocalDateTime.now()
        );
        userLocationRepository.save(location);

        // Calibrate network coordinates if defaulted to North America (NYC)
        calibrateNetworkToUserLocation(request.getLatitude(), request.getLongitude());

        // Broadcast user position over WebSocket for admin tracking
        Map<String, Object> locationEvent = new HashMap<>();
        locationEvent.put("sessionId", request.getSessionId());
        locationEvent.put("lat", request.getLatitude());
        locationEvent.put("lng", request.getLongitude());
        locationEvent.put("accuracy", request.getAccuracy());
        locationEvent.put("timestamp", LocalDateTime.now().toString());
        try {
            messagingTemplate.convertAndSend("/topic/user-locations", locationEvent);
        } catch (Exception ignored) {}

        return findNearby(request.getLatitude(), request.getLongitude());
    }

    @Transactional
    public void calibrateNetworkToUserLocation(double userLat, double userLng) {
        List<Bus> buses = busRepository.findAll();
        List<BusRoute> routes = routeRepository.findAll();
        List<BusStop> stops = busStopRepository.findAll();

        if (buses.isEmpty() || routes.isEmpty()) return;

        double firstLat = buses.get(0).getLatitude();
        double firstLng = buses.get(0).getLongitude();

        double distKm = haversineKm(userLat, userLng, firstLat, firstLng);
        if (distKm < 10.0) return; // Already in user's region

        double deltaLat = userLat - firstLat;
        double deltaLng = userLng - firstLng;

        for (Bus bus : buses) {
            bus.setLatitude(bus.getLatitude() + deltaLat);
            bus.setLongitude(bus.getLongitude() + deltaLng);
            busRepository.save(bus);
        }

        for (BusStop stop : stops) {
            stop.setLatitude(stop.getLatitude() + deltaLat);
            stop.setLongitude(stop.getLongitude() + deltaLng);
            busStopRepository.save(stop);
        }

        for (BusRoute route : routes) {
            try {
                if (route.getPathJson() != null && !route.getPathJson().isEmpty()) {
                    double[][] path = objectMapper.readValue(route.getPathJson(), double[][].class);
                    for (double[] point : path) {
                        point[0] += deltaLat;
                        point[1] += deltaLng;
                    }
                    route.setPathJson(objectMapper.writeValueAsString(path));
                    routeRepository.save(route);
                }
            } catch (Exception ignored) {}
        }
    }

    @Transactional(readOnly = true)
    public NearbyResultDto findNearby(double userLat, double userLng) {

        // Build route map for enrichment
        List<BusRoute> allRoutes = routeRepository.findAll();
        Map<String, BusRoute> routeMap = new HashMap<>();
        for (BusRoute r : allRoutes) routeMap.put(r.getId(), r);

        // Build stop-to-route map
        List<BusStop> allStops = busStopRepository.findAll();
        Map<String, BusRoute> stopRouteMap = new HashMap<>();
        for (BusStop stop : allStops) {
            if (stop.getRoute() != null) stopRouteMap.put(stop.getId(), stop.getRoute());
        }

        // Find nearby buses (sorted by distance, max 5 km)
        List<Bus> allBuses = busRepository.findAll();
        List<NearbyBusDto> nearbyBuses = allBuses.stream()
                .map(bus -> {
                    double distKm = haversineKm(userLat, userLng, bus.getLatitude(), bus.getLongitude());
                    return distKm <= MAX_NEARBY_RADIUS_KM ? buildNearbyBusDto(bus, distKm, routeMap, stopRouteMap, allStops) : null;
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingDouble(NearbyBusDto::getDistanceMeters))
                .limit(5)
                .collect(Collectors.toList());

        // Find nearby stops (sorted by walking distance, max 3 km)
        List<NearbyStopDto> nearbyStops = allStops.stream()
                .map(stop -> {
                    double distKm = haversineKm(userLat, userLng, stop.getLatitude(), stop.getLongitude());
                    return distKm <= 3.0 ? buildNearbyStopDto(stop, distKm, stopRouteMap) : null;
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingDouble(NearbyStopDto::getDistanceMeters))
                .limit(8)
                .collect(Collectors.toList());

        return new NearbyResultDto(userLat, userLng, nearbyBuses, nearbyStops);
    }

    private NearbyBusDto buildNearbyBusDto(Bus bus, double distKm, Map<String, BusRoute> routeMap,
                                            Map<String, BusRoute> stopRouteMap, List<BusStop> allStops) {
        NearbyBusDto dto = new NearbyBusDto();
        dto.setBusId(bus.getId());
        dto.setPlate(bus.getPlate());
        dto.setDriverName(bus.getDriverName());
        dto.setRouteId(bus.getRouteId());
        dto.setStatus(bus.getStatus());
        dto.setDelayInSeconds(bus.getDelayInSeconds());
        dto.setSpeed(bus.getSpeed());
        dto.setPassengers(bus.getPassengers());
        dto.setCapacity(bus.getCapacity());
        dto.setLocation(new double[]{bus.getLatitude(), bus.getLongitude()});
        dto.setNextStopId(bus.getNextStopId());
        dto.setDistanceMeters(distKm * 1000);

        // ETA = distance / speed in seconds (bus speed in km/h)
        double effectiveSpeed = bus.getSpeed() > 0 ? bus.getSpeed() : 20;
        int eta = (int) ((distKm / effectiveSpeed) * 3600);
        if (bus.getDelayInSeconds() > 0) eta += bus.getDelayInSeconds();
        dto.setEtaSeconds(eta);

        BusRoute route = routeMap.get(bus.getRouteId());
        if (route != null) {
            dto.setRouteName(route.getName());
            dto.setRouteColor(route.getColor());
        }

        // Find next stop name
        allStops.stream()
                .filter(s -> s.getId().equals(bus.getNextStopId()))
                .findFirst()
                .ifPresent(s -> dto.setNextStopName(s.getName()));

        return dto;
    }

    private NearbyStopDto buildNearbyStopDto(BusStop stop, double distKm, Map<String, BusRoute> stopRouteMap) {
        NearbyStopDto dto = new NearbyStopDto();
        dto.setStopId(stop.getId());
        dto.setStopName(stop.getName());
        dto.setLocation(new double[]{stop.getLatitude(), stop.getLongitude()});
        dto.setDistanceMeters(distKm * 1000);

        // Walk time = distance / walk speed in seconds
        int walkSecs = (int) ((distKm / WALK_SPEED_KMH) * 3600);
        dto.setWalkTimeSeconds(walkSecs);

        BusRoute route = stopRouteMap.get(stop.getId());
        if (route != null) {
            dto.setRouteId(route.getId());
            dto.setRouteName(route.getName());
            dto.setRouteColor(route.getColor());
        }

        return dto;
    }

    /**
     * Haversine formula: distance between two GPS coordinates in kilometers.
     */
    public static double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    public String getLocationApiKey() {
        return locationApiKey;
    }
}

