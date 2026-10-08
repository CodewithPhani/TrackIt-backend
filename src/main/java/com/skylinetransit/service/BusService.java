package com.skylinetransit.service;

import com.skylinetransit.dto.BusDto;
import com.skylinetransit.dto.IncidentReportRequest;
import com.skylinetransit.model.Bus;
import com.skylinetransit.model.IncidentAlert;
import com.skylinetransit.repository.BusRepository;
import com.skylinetransit.repository.IncidentAlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusService {

    private final BusRepository busRepository;
    private final IncidentAlertRepository incidentAlertRepository;

    public BusService(BusRepository busRepository, IncidentAlertRepository incidentAlertRepository) {
        this.busRepository = busRepository;
        this.incidentAlertRepository = incidentAlertRepository;
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
        return new BusDto(
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
    }
}
