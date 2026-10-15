package com.skylinetransit.service;

import com.skylinetransit.dto.BusDto;
import com.skylinetransit.dto.BusLocationUpdateRequest;
import com.skylinetransit.dto.IncidentReportRequest;
import com.skylinetransit.model.Bus;
import com.skylinetransit.model.IncidentAlert;
import com.skylinetransit.repository.BusRepository;
import com.skylinetransit.repository.IncidentAlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusService {

    private final BusRepository busRepository;
    private final IncidentAlertRepository incidentAlertRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public BusService(BusRepository busRepository, IncidentAlertRepository incidentAlertRepository, SimpMessagingTemplate messagingTemplate) {
        this.busRepository = busRepository;
        this.incidentAlertRepository = incidentAlertRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional(readOnly = true)
    public List<BusDto> getAllBuses() {
        return busRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BusDto getBusById(String id) {
        return busRepository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }


    @Transactional
    public BusDto updateGpsLocation(String id, BusLocationUpdateRequest request) {
        if (!Double.isFinite(request.getLatitude()) || request.getLatitude() < -90 || request.getLatitude() > 90
                || !Double.isFinite(request.getLongitude()) || request.getLongitude() < -180 || request.getLongitude() > 180) {
            throw new IllegalArgumentException("Invalid GPS coordinates");
        }

        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with id: " + id));
        bus.setLatitude(request.getLatitude());
        bus.setLongitude(request.getLongitude());
        bus.setHeading(Double.isFinite(request.getHeading()) ? request.getHeading() : 0);
        bus.setGpsAccuracy(Double.isFinite(request.getAccuracy()) ? Math.max(0, request.getAccuracy()) : 0);
        bus.setSpeed(Math.max(0, request.getSpeed()) * 3.6);
        bus.setLocationSource("gps");
        bus.setLastLocationUpdate(LocalDateTime.now());
        BusDto dto = toDto(busRepository.save(bus));
        try { messagingTemplate.convertAndSend("/topic/bus-locations", dto); } catch (Exception ignored) { }
        return dto;
    }

    @Transactional
    public BusDto reportIncident(IncidentReportRequest request) {
        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with id: " + request.getBusId()));

        bus.setStatus("delayed");
        bus.setDelayInSeconds(request.getDelayInSeconds() > 0 ? request.getDelayInSeconds() : 600); // default 10 mins
        bus.setSpeed(0);
        Bus savedBus = busRepository.save(bus);

        String alertType = request.getAlertType() != null ? request.getAlertType() : "emergency";
        String message = request.getMessage() != null ? request.getMessage() : "Incident reported by driver. 10m delay expected.";

        IncidentAlert alert = new IncidentAlert(bus.getId(), alertType, message, LocalDateTime.now());
        incidentAlertRepository.save(alert);

        return toDto(savedBus);
    }

    public BusDto toDto(Bus bus) {
        if (bus == null) return null;
        BusDto dto = new BusDto(
                bus.getId(),
                bus.getRouteId(),
                bus.getPlate(),
                bus.getDriverName(),
                bus.getCapacity(),
                bus.getPassengers(),
                new double[]{bus.getLatitude(), bus.getLongitude()},
                bus.getSpeed(),
                bus.getHeading(),
                bus.getNextStopId(),
                bus.getStatus(),
                bus.getDelayInSeconds()
        );
        dto.setLocationSource(bus.getLocationSource() == null ? "simulation" : bus.getLocationSource());
        dto.setLastLocationUpdate(bus.getLastLocationUpdate() == null ? null : bus.getLastLocationUpdate().toString());
        dto.setGpsAccuracy(bus.getGpsAccuracy());
        return dto;
    }
}
