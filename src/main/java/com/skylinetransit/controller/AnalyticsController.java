package com.skylinetransit.controller;

import com.skylinetransit.dto.FleetAnalyticsDto;
import com.skylinetransit.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ResponseEntity<FleetAnalyticsDto> getFleetAnalytics() {
        return ResponseEntity.ok(analyticsService.getFleetAnalytics());
    }
}
