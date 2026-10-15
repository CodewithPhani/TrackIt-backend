package com.skylinetransit.controller;

import com.skylinetransit.dto.RouteDto;
import com.skylinetransit.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<List<RouteDto>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteDto> getRouteById(@PathVariable String id) {
        RouteDto route = routeService.getRouteById(id);
        if (route == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(route);
    }
}
