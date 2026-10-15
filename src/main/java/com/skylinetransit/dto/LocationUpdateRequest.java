package com.skylinetransit.dto;

public class LocationUpdateRequest {
    private String sessionId;
    private double latitude;
    private double longitude;
    private double accuracy;    // meters
    private double heading;     // degrees 0-360
    private double speed;       // m/s

    public LocationUpdateRequest() {}

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getAccuracy() { return accuracy; }
    public void setAccuracy(double accuracy) { this.accuracy = accuracy; }

    public double getHeading() { return heading; }
    public void setHeading(double heading) { this.heading = heading; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
}
