package com.skylinetransit.dto;

public class FleetAnalyticsDto {
    private int activeBuses;
    private int delayedBuses;
    private int totalPassengers;
    private int onTimePercentage;
    private int activeAlertsCount;

    public FleetAnalyticsDto() {}

    public FleetAnalyticsDto(int activeBuses, int delayedBuses, int totalPassengers, int onTimePercentage, int activeAlertsCount) {
        this.activeBuses = activeBuses;
        this.delayedBuses = delayedBuses;
        this.totalPassengers = totalPassengers;
        this.onTimePercentage = onTimePercentage;
        this.activeAlertsCount = activeAlertsCount;
    }

    public int getActiveBuses() { return activeBuses; }
    public void setActiveBuses(int activeBuses) { this.activeBuses = activeBuses; }

    public int getDelayedBuses() { return delayedBuses; }
    public void setDelayedBuses(int delayedBuses) { this.delayedBuses = delayedBuses; }

    public int getTotalPassengers() { return totalPassengers; }
    public void setTotalPassengers(int totalPassengers) { this.totalPassengers = totalPassengers; }

    public int getOnTimePercentage() { return onTimePercentage; }
    public void setOnTimePercentage(int onTimePercentage) { this.onTimePercentage = onTimePercentage; }

    public int getActiveAlertsCount() { return activeAlertsCount; }
    public void setActiveAlertsCount(int activeAlertsCount) { this.activeAlertsCount = activeAlertsCount; }
}
