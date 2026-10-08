package com.skylinetransit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skylinetransit.dto.BusStopDto;
import com.skylinetransit.dto.RouteDto;
import com.skylinetransit.model.BusRoute;
import com.skylinetransit.repository.RouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final ObjectMapper objectMapper;

    public RouteService(RouteRepository routeRepository, ObjectMapper objectMapper) {
        this.routeRepository = routeRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public List<RouteDto> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RouteDto getRouteById(String id) {
        return routeRepository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    public RouteDto toDto(BusRoute route) {
        if (route == null) return null;

        List<double[]> pathList = new ArrayList<>();
        try {
            if (route.getPathJson() != null && !route.getPathJson().isEmpty()) {
                double[][] arr = objectMapper.readValue(route.getPathJson(), double[][].class);
                pathList = Arrays.asList(arr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<BusStopDto> stopDtos = route.getStops().stream()
                .map(stop -> new BusStopDto(stop.getId(), stop.getName(), new double[]{stop.getLatitude(), stop.getLongitude()}))
                .collect(Collectors.toList());

        return new RouteDto(
                route.getId(),
                route.getName(),
                route.getType(),
                route.getColor(),
                pathList,
                stopDtos
        );
    }
}
