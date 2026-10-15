package com.skylinetransit.controller;

import com.skylinetransit.dto.BusDto;
import com.skylinetransit.dto.BusLocationUpdateRequest;
import com.skylinetransit.dto.IncidentReportRequest;
import com.skylinetransit.service.BusService;
import com.skylinetransit.auth.AuthSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
public class BusController {

    private final BusService busService;
    private final AuthSessionService sessions;

    public BusController(BusService busService, AuthSessionService sessions) {
        this.busService = busService;
        this.sessions = sessions;
    }

    @GetMapping
    public ResponseEntity<List<BusDto>> getAllBuses() {
        return ResponseEntity.ok(busService.getAllBuses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusDto> getBusById(@PathVariable String id) {
        BusDto bus = busService.getBusById(id);
        if (bus == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bus);
    }

    @PostMapping("/{id}/location")
    public ResponseEntity<?> updateLocation(@PathVariable String id, @RequestBody BusLocationUpdateRequest request, @RequestHeader(value = "Authorization", required = false) String authorization) {
        AuthSessionService.SessionUser user = sessions.resolve(authorization);
        if (user == null || !user.role().equals("driver")) return ResponseEntity.status(401).body(java.util.Map.of("message", "A driver account is required to update bus GPS."));
        if (user.assignedBusId() == null || !user.assignedBusId().equalsIgnoreCase(id)) return ResponseEntity.status(403).body(java.util.Map.of("message", "This driver is not assigned to that bus."));
        return ResponseEntity.ok(busService.updateGpsLocation(id, request));
    }

    @PostMapping("/{id}/incident")
    public ResponseEntity<?> reportIncident(@PathVariable String id, @RequestBody(required = false) IncidentReportRequest request, @RequestHeader(value = "Authorization", required = false) String authorization) {
        AuthSessionService.SessionUser user = sessions.resolve(authorization);
        if (user == null || !user.role().equals("driver")) return ResponseEntity.status(401).body(java.util.Map.of("message", "A driver account is required to report an incident."));
        if (user.assignedBusId() == null || !user.assignedBusId().equalsIgnoreCase(id)) return ResponseEntity.status(403).body(java.util.Map.of("message", "This driver is not assigned to that bus."));
        if (request == null) {
            request = new IncidentReportRequest();
        }
        request.setBusId(id);
        BusDto updatedBus = busService.reportIncident(request);
        return ResponseEntity.ok(updatedBus);
    }
}
