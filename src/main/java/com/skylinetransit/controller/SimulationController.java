package com.skylinetransit.controller;

import com.skylinetransit.service.SimulationEngineService;
import com.skylinetransit.auth.AuthSessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    private final SimulationEngineService simulationEngineService;
    private final AuthSessionService sessions;

    public SimulationController(SimulationEngineService simulationEngineService, AuthSessionService sessions) {
        this.simulationEngineService = simulationEngineService;
        this.sessions = sessions;
    }
    private boolean isAdmin(HttpServletRequest request) { return sessions.hasRole(request.getHeader("Authorization"), "admin"); }

    @GetMapping("/state")
    public ResponseEntity<Map<String, Object>> getSimulationState() {
        Map<String, Object> state = new HashMap<>();
        state.put("isPlaying", simulationEngineService.isPlaying());
        state.put("simulationSpeed", simulationEngineService.getSimulationSpeed());
        return ResponseEntity.ok(state);
    }

    @PostMapping("/speed")
    public ResponseEntity<Map<String, Object>> setSpeed(@RequestParam int speed, HttpServletRequest request) {
        if (!isAdmin(request)) return ResponseEntity.status(403).build();
        simulationEngineService.setSimulationSpeed(speed);
        return getSimulationState();
    }

    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleSimulation(HttpServletRequest request) {
        if (!isAdmin(request)) return ResponseEntity.status(403).build();
        simulationEngineService.setPlaying(!simulationEngineService.isPlaying());
        return getSimulationState();
    }
}
