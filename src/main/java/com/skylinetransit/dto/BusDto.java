package com.skylinetransit.dto;

public class BusDto {
    private String id;
    private String routeId;
    private String plate;
    private String driverName;
    private int capacity;
    private int passengers;
    private double[] location; // [lat, lng]
    private double speed;
    private double heading;
    private String nextStopId;
    private String status;
    private long delayInSeconds;

    public BusDto() {}

    public BusDto(String id, String routeId, String plate, String driverName, int capacity, int passengers,
                  double[] location, double speed, double heading, String nextStopId, String status, long delayInSeconds) {
        this.id = id;
        this.routeId = routeId;
        this.plate = plate;
        this.driverName = driverName;
        this.capacity = capacity;
        this.passengers = passengers;
        this.location = location;
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

    public double[] getLocation() { return location; }
    public void setLocation(double[] location) { this.location = location; }

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
