package com.skylinetransit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skylinetransit.dto.BusDto;
import com.skylinetransit.model.Bus;
import com.skylinetransit.model.BusRoute;
import com.skylinetransit.repository.BusRepository;
import com.skylinetransit.repository.RouteRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SimulationEngineService {

    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final BusService busService;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    private int simulationSpeed = 1;
    private boolean isPlaying = true;

    public SimulationEngineService(BusRepository busRepository,
                                   RouteRepository routeRepository,
                                   BusService busService,
                                   ObjectMapper objectMapper,
                                   SimpMessagingTemplate messagingTemplate) {
        this.busRepository = busRepository;
        this.routeRepository = routeRepository;
        this.busService = busService;
        this.objectMapper = objectMapper;
        this.messagingTemplate = messagingTemplate;
    }

    public int getSimulationSpeed() { return simulationSpeed; }
    public void setSimulationSpeed(int simulationSpeed) { this.simulationSpeed = simulationSpeed; }

    public boolean isPlaying() { return isPlaying; }
    public void setPlaying(boolean playing) { isPlaying = playing; }

    @Scheduled(fixedRate = 2000)
    @Transactional
    public void performSimulationTick() {
        if (!isPlaying) return;

        List<Bus> buses = busRepository.findAll();
        List<BusRoute> routes = routeRepository.findAll();
        Map<String, BusRoute> routeMap = new HashMap<>();
        for (BusRoute r : routes) routeMap.put(r.getId(), r);

        double deltaHours = (2.0 * simulationSpeed) / 3600.0;
        List<BusDto> updatedBusDtos = new ArrayList<>();

        for (Bus bus : buses) {
            if ("stopped".equalsIgnoreCase(bus.getStatus()) || bus.getDelayInSeconds() > 0) {
                long newDelay = Math.max(0, bus.getDelayInSeconds() - (2L * simulationSpeed));
                bus.setDelayInSeconds(newDelay);
                bus.setStatus(newDelay == 0 ? "on_time" : "delayed");
                bus.setSpeed(newDelay > 0 ? 0 : Math.floor(Math.random() * 30 + 20));
                busRepository.save(bus);
                updatedBusDtos.add(busService.toDto(bus));
                continue;
            }

            BusRoute route = routeMap.get(bus.getRouteId());
            if (route == null) {
                updatedBusDtos.add(busService.toDto(bus));
                continue;
            }

            List<double[]> pathPoints = parsePath(route.getPathJson());
            if (pathPoints.isEmpty()) {
                updatedBusDtos.add(busService.toDto(bus));
                continue;
            }

            PathStep step = getNextPathPointAndHeading(bus.getLatitude(), bus.getLongitude(), pathPoints);
            double distToMove = bus.getSpeed() * deltaHours;

            double newLat = bus.getLatitude();
            double newLng = bus.getLongitude();

            if (step.distToTarget <= distToMove || step.distToTarget < 0.001) {
                newLat = step.target[0];
                newLng = step.target[1];
            } else {
                double ratio = distToMove / step.distToTarget;
                newLat = bus.getLatitude() + (step.target[0] - bus.getLatitude()) * ratio;
                newLng = bus.getLongitude() + (step.target[1] - bus.getLongitude()) * ratio;
            }

            double newSpeed = bus.getSpeed() + (Math.random() > 0.5 ? 2 : -2);
            newSpeed = Math.max(15, Math.min(newSpeed, 80));

            int newPassengers = bus.getPassengers();
            if (Math.random() > 0.90 && step.distToTarget < 0.01) {
                int change = (int) (Math.random() * 10) - 4;
                newPassengers = Math.max(0, Math.min(bus.getCapacity(), newPassengers + change));
            }

            bus.setLatitude(newLat);
            bus.setLongitude(newLng);
            bus.setHeading(step.heading);
            bus.setSpeed(Math.round(newSpeed));
            bus.setPassengers(newPassengers);

            busRepository.save(bus);
            updatedBusDtos.add(busService.toDto(bus));
        }

        // Broadcast telemetry over WebSocket
        try {
            messagingTemplate.convertAndSend("/topic/bus-locations", updatedBusDtos);
        } catch (Exception ignored) {
            // Safe fallback if websocket broker is not subscribed yet
        }
    }

    private List<double[]> parsePath(String json) {
        try {
            if (json != null && !json.isEmpty()) {
                double[][] arr = objectMapper.readValue(json, double[][].class);
                return Arrays.asList(arr);
            }
            return Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private PathStep getNextPathPointAndHeading(double busLat, double busLng, List<double[]> path) {
        double minDistance = Double.MAX_VALUE;
        int nearestIndex = 0;

        for (int i = 0; i < path.size(); i++) {
            double dist = getDistanceInKm(busLat, busLng, path.get(i)[0], path.get(i)[1]);
            if (dist < minDistance) {
                minDistance = dist;
                nearestIndex = i;
            }
        }

        int nextIndex = (nearestIndex + 1) % path.size();
        double[] target = path.get(nextIndex);

        double dLon = (target[1] - busLng);
        double y = Math.sin(dLon) * Math.cos(target[0]);
        double x = Math.cos(busLat) * Math.sin(target[0]) - Math.sin(busLat) * Math.cos(target[0]) * Math.cos(dLon);
        double brng = Math.atan2(y, x) * (180.0 / Math.PI);
        double heading = (brng + 360.0) % 360.0;

        double distToTarget = getDistanceInKm(busLat, busLng, target[0], target[1]);
        return new PathStep(target, heading, distToTarget);
    }

    private double getDistanceInKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private static class PathStep {
        double[] target;
        double heading;
        double distToTarget;

        PathStep(double[] target, double heading, double distToTarget) {
            this.target = target;
            this.heading = heading;
            this.distToTarget = distToTarget;
        }
    }
}
