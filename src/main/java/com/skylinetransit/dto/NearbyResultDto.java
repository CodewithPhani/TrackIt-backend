package com.skylinetransit.dto;

import java.util.List;

public class NearbyResultDto {
    private double userLat;
    private double userLng;
    private List<NearbyBusDto> nearbyBuses;
    private List<NearbyStopDto> nearbyStops;

    public NearbyResultDto() {}

    public NearbyResultDto(double userLat, double userLng, List<NearbyBusDto> nearbyBuses, List<NearbyStopDto> nearbyStops) {
        this.userLat = userLat;
        this.userLng = userLng;
        this.nearbyBuses = nearbyBuses;
        this.nearbyStops = nearbyStops;
    }

    public double getUserLat() { return userLat; }
    public void setUserLat(double userLat) { this.userLat = userLat; }

    public double getUserLng() { return userLng; }
    public void setUserLng(double userLng) { this.userLng = userLng; }

    public List<NearbyBusDto> getNearbyBuses() { return nearbyBuses; }
    public void setNearbyBuses(List<NearbyBusDto> nearbyBuses) { this.nearbyBuses = nearbyBuses; }

    public List<NearbyStopDto> getNearbyStops() { return nearbyStops; }
    public void setNearbyStops(List<NearbyStopDto> nearbyStops) { this.nearbyStops = nearbyStops; }

    // --- Nested DTO: NearbyBusDto ---
    public static class NearbyBusDto {
        private String busId;
        private String plate;
        private String driverName;
        private String routeId;
        private String routeName;
        private String routeColor;
        private double distanceMeters;
        private int etaSeconds;       // estimated arrival at nearest stop
        private String status;
        private long delayInSeconds;
        private double speed;
        private int passengers;
        private int capacity;
        private double[] location;
        private String nextStopId;
        private String nextStopName;

        public NearbyBusDto() {}

        public String getBusId() { return busId; }
        public void setBusId(String busId) { this.busId = busId; }

        public String getPlate() { return plate; }
        public void setPlate(String plate) { this.plate = plate; }

        public String getDriverName() { return driverName; }
        public void setDriverName(String driverName) { this.driverName = driverName; }

        public String getRouteId() { return routeId; }
        public void setRouteId(String routeId) { this.routeId = routeId; }

        public String getRouteName() { return routeName; }
        public void setRouteName(String routeName) { this.routeName = routeName; }

        public String getRouteColor() { return routeColor; }
        public void setRouteColor(String routeColor) { this.routeColor = routeColor; }

        public double getDistanceMeters() { return distanceMeters; }
        public void setDistanceMeters(double distanceMeters) { this.distanceMeters = distanceMeters; }

        public int getEtaSeconds() { return etaSeconds; }
        public void setEtaSeconds(int etaSeconds) { this.etaSeconds = etaSeconds; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public long getDelayInSeconds() { return delayInSeconds; }
        public void setDelayInSeconds(long delayInSeconds) { this.delayInSeconds = delayInSeconds; }

        public double getSpeed() { return speed; }
        public void setSpeed(double speed) { this.speed = speed; }

        public int getPassengers() { return passengers; }
        public void setPassengers(int passengers) { this.passengers = passengers; }

        public int getCapacity() { return capacity; }
        public void setCapacity(int capacity) { this.capacity = capacity; }

        public double[] getLocation() { return location; }
        public void setLocation(double[] location) { this.location = location; }

        public String getNextStopId() { return nextStopId; }
        public void setNextStopId(String nextStopId) { this.nextStopId = nextStopId; }

        public String getNextStopName() { return nextStopName; }
        public void setNextStopName(String nextStopName) { this.nextStopName = nextStopName; }
    }

    // --- Nested DTO: NearbyStopDto ---
    public static class NearbyStopDto {
        private String stopId;
        private String stopName;
        private String routeId;
        private String routeName;
        private String routeColor;
        private double[] location;
        private double distanceMeters;
        private int walkTimeSeconds; // walking ETA

        public NearbyStopDto() {}

        public String getStopId() { return stopId; }
        public void setStopId(String stopId) { this.stopId = stopId; }

        public String getStopName() { return stopName; }
        public void setStopName(String stopName) { this.stopName = stopName; }

        public String getRouteId() { return routeId; }
        public void setRouteId(String routeId) { this.routeId = routeId; }

        public String getRouteName() { return routeName; }
        public void setRouteName(String routeName) { this.routeName = routeName; }

        public String getRouteColor() { return routeColor; }
        public void setRouteColor(String routeColor) { this.routeColor = routeColor; }

        public double[] getLocation() { return location; }
        public void setLocation(double[] location) { this.location = location; }

        public double getDistanceMeters() { return distanceMeters; }
        public void setDistanceMeters(double distanceMeters) { this.distanceMeters = distanceMeters; }

        public int getWalkTimeSeconds() { return walkTimeSeconds; }
        public void setWalkTimeSeconds(int walkTimeSeconds) { this.walkTimeSeconds = walkTimeSeconds; }
    }
}
