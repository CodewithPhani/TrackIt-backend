package com.skylinetransit.controller;

import com.skylinetransit.dto.LocationUpdateRequest;
import com.skylinetransit.dto.NearbyResultDto;
import com.skylinetransit.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * POST /api/location/update
     * Receives the user's real-time GPS coordinates, persists them,
     * broadcasts to WebSocket topic, and returns nearby buses/stops.
     */
    @PostMapping("/update")
    public ResponseEntity<NearbyResultDto> updateLocation(@RequestBody LocationUpdateRequest request) {
        NearbyResultDto result = locationService.updateLocationAndFindNearby(request);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/location/nearby?lat=&lng=
     * Pure read endpoint: find nearest buses and stops for given coordinates.
     */
    @GetMapping("/nearby")
    public ResponseEntity<NearbyResultDto> getNearby(
            @RequestParam double lat,
            @RequestParam double lng) {
        NearbyResultDto result = locationService.findNearby(lat, lng);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/location/config
     * Returns location service configuration including active API key.
     */
    @GetMapping("/config")
    public ResponseEntity<java.util.Map<String, String>> getLocationConfig() {
        java.util.Map<String, String> config = new java.util.HashMap<>();
        config.put("locationApiKey", locationService.getLocationApiKey());
        config.put("status", "ACTIVE");
        return ResponseEntity.ok(config);
    }
}

