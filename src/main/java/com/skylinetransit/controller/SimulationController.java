package com.skylinetransit.controller;

import com.skylinetransit.service.SimulationEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    private final SimulationEngineService simulationEngineService;

    public SimulationController(SimulationEngineService simulationEngineService) {
        this.simulationEngineService = simulationEngineService;
    }

    @GetMapping("/state")
    public ResponseEntity<Map<String, Object>> getSimulationState() {
        Map<String, Object> state = new HashMap<>();
        state.put("isPlaying", simulationEngineService.isPlaying());
        state.put("simulationSpeed", simulationEngineService.getSimulationSpeed());
        return ResponseEntity.ok(state);
    }

    @PostMapping("/speed")
    public ResponseEntity<Map<String, Object>> setSpeed(@RequestParam int speed) {
        simulationEngineService.setSimulationSpeed(speed);
        return getSimulationState();
    }

    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleSimulation() {
        simulationEngineService.setPlaying(!simulationEngineService.isPlaying());
        return getSimulationState();
    }
}
