package com.skylinetransit.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bus_routes")
public class BusRoute {

    @Id
    private String id;

    private String name;
    private String type;
    private String color;

    @Column(columnDefinition = "TEXT")
    private String pathJson; // JSON representation of [[lat, lng], ...]

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("id ASC")
    private List<BusStop> stops = new ArrayList<>();

    public BusRoute() {}

    public BusRoute(String id, String name, String type, String color, String pathJson) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.color = color;
        this.pathJson = pathJson;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getPathJson() { return pathJson; }
    public void setPathJson(String pathJson) { this.pathJson = pathJson; }

    public List<BusStop> getStops() { return stops; }
    public void setStops(List<BusStop> stops) { this.stops = stops; }

    public void addStop(BusStop stop) {
        stops.add(stop);
        stop.setRoute(this);
    }
}
