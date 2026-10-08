package com.skylinetransit.dto;

public class IncidentReportRequest {
    private String busId;
    private long delayInSeconds;
    private String alertType; // "traffic", "mechanical", "emergency"
    private String message;

    public IncidentReportRequest() {}

    public IncidentReportRequest(String busId, long delayInSeconds, String alertType, String message) {
        this.busId = busId;
        this.delayInSeconds = delayInSeconds;
        this.alertType = alertType;
        this.message = message;
    }

    public String getBusId() { return busId; }
    public void setBusId(String busId) { this.busId = busId; }

    public long getDelayInSeconds() { return delayInSeconds; }
    public void setDelayInSeconds(long delayInSeconds) { this.delayInSeconds = delayInSeconds; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
