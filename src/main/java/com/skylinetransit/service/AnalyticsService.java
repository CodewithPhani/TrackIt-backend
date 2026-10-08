package com.skylinetransit.service;

import com.skylinetransit.dto.FleetAnalyticsDto;
import com.skylinetransit.model.Bus;
import com.skylinetransit.repository.BusRepository;
import com.skylinetransit.repository.IncidentAlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnalyticsService {

    private final BusRepository busRepository;
    private final IncidentAlertRepository incidentAlertRepository;

    public AnalyticsService(BusRepository busRepository, IncidentAlertRepository incidentAlertRepository) {
        this.busRepository = busRepository;
        this.incidentAlertRepository = incidentAlertRepository;
    }

    @Transactional(readOnly = true)
    public FleetAnalyticsDto getFleetAnalytics() {
        List<Bus> buses = busRepository.findAll();
        int activeBuses = buses.size();
        if (activeBuses == 0) {
            return new FleetAnalyticsDto(0, 0, 0, 100, 0);
        }

        int delayedBuses = (int) buses.stream().filter(b -> b.getDelayInSeconds() > 0 || "delayed".equalsIgnoreCase(b.getStatus())).count();
        int totalPassengers = buses.stream().mapToInt(Bus::getPassengers).sum();
        int onTimePercentage = Math.round((float) (activeBuses - delayedBuses) / activeBuses * 100);
        int activeAlertsCount = (int) incidentAlertRepository.count();

        return new FleetAnalyticsDto(activeBuses, delayedBuses, totalPassengers, onTimePercentage, activeAlertsCount);
    }
}
