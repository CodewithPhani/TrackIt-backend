package com.skylinetransit.controller;

import com.skylinetransit.model.IncidentAlert;
import com.skylinetransit.repository.IncidentAlertRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class IncidentController {

    private final IncidentAlertRepository incidentAlertRepository;

    public IncidentController(IncidentAlertRepository incidentAlertRepository) {
        this.incidentAlertRepository = incidentAlertRepository;
    }

    @GetMapping
    public ResponseEntity<List<IncidentAlert>> getRecentAlerts() {
        return ResponseEntity.ok(incidentAlertRepository.findTop20ByOrderByTimestampDesc());
    }

    @GetMapping("/bus/{busId}")
    public ResponseEntity<List<IncidentAlert>> getAlertsForBus(@PathVariable String busId) {
        return ResponseEntity.ok(incidentAlertRepository.findByBusIdOrderByTimestampDesc(busId));
    }
}
