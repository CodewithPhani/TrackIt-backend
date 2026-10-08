package com.skylinetransit.model;

import jakarta.persistence.*;

@Entity
@Table(name = "buses")
public class Bus {

    @Id
    private String id;

    private String routeId;
    private String plate;
    private String driverName;
    private int capacity;
    private int passengers;

    private double latitude;
    private double longitude;
    private double speed;
    private double heading;

    private String nextStopId;
    private String status; // "on_time", "delayed", "early", "stopped"
    private long delayInSeconds;

    public Bus() {}

    public Bus(String id, String routeId, String plate, String driverName, int capacity, int passengers,
               double latitude, double longitude, double speed, double heading, String nextStopId,
               String status, long delayInSeconds) {
        this.id = id;
        this.routeId = routeId;
        this.plate = plate;
        this.driverName = driverName;
        this.capacity = capacity;
        this.passengers = passengers;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.heading = heading;
        this.nextStopId = nextStopId;
        this.status = status;
        this.delayInSeconds = delayInSeconds;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }

    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getPassengers() { return passengers; }
    public void setPassengers(int passengers) { this.passengers = passengers; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }

    public double getHeading() { return heading; }
    public void setHeading(double heading) { this.heading = heading; }

    public String getNextStopId() { return nextStopId; }
    public void setNextStopId(String nextStopId) { this.nextStopId = nextStopId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getDelayInSeconds() { return delayInSeconds; }
    public void setDelayInSeconds(long delayInSeconds) { this.delayInSeconds = delayInSeconds; }
}
