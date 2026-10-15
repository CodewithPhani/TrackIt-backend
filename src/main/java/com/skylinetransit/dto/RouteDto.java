package com.skylinetransit.dto;

import java.util.List;

public class RouteDto {
    private String id;
    private String name;
    private String type;
    private String color;
    private List<double[]> path; // List of [lat, lng] points
    private List<BusStopDto> stops;

    public RouteDto() {}

    public RouteDto(String id, String name, String type, String color, List<double[]> path, List<BusStopDto> stops) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.color = color;
        this.path = path;
        this.stops = stops;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public List<double[]> getPath() { return path; }
    public void setPath(List<double[]> path) { this.path = path; }

    public List<BusStopDto> getStops() { return stops; }
    public void setStops(List<BusStopDto> stops) { this.stops = stops; }
}
