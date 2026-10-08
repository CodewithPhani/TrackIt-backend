package com.skylinetransit.controller;

import com.skylinetransit.dto.BusDto;
import com.skylinetransit.dto.IncidentReportRequest;
import com.skylinetransit.service.BusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
public class BusController {

    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
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

    @PostMapping("/{id}/incident")
    public ResponseEntity<BusDto> reportIncident(@PathVariable String id, @RequestBody(required = false) IncidentReportRequest request) {
        if (request == null) {
            request = new IncidentReportRequest();
        }
        request.setBusId(id);
        BusDto updatedBus = busService.reportIncident(request);
        return ResponseEntity.ok(updatedBus);
    }
}
