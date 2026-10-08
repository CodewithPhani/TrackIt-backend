package com.skylinetransit.config;

import com.skylinetransit.model.Bus;
import com.skylinetransit.model.BusRoute;
import com.skylinetransit.model.BusStop;
import com.skylinetransit.repository.BusRepository;
import com.skylinetransit.repository.RouteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RouteRepository routeRepository;
    private final BusRepository busRepository;

    public DataInitializer(RouteRepository routeRepository,
                           BusRepository busRepository) {
        this.routeRepository = routeRepository;
        this.busRepository = busRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (routeRepository.count() > 0) {
            return;
        }

        double baseLat = 40.7128;
        double baseLng = -74.0060;

        // --- Route 1: R101 Downtown Circular ---
        BusRoute r101 = new BusRoute(
                "R101",
                "101 Downtown Circular",
                "Circular",
                "#3b82f6",
                "[[" + baseLat + "," + baseLng + "],[" + (baseLat + 0.005) + "," + (baseLng + 0.01) + "],[" + (baseLat + 0.015) + "," + (baseLng + 0.012) + "],[" + (baseLat + 0.02) + "," + (baseLng + 0.005) + "],[" + (baseLat + 0.015) + "," + (baseLng - 0.005) + "],[" + (baseLat + 0.008) + "," + (baseLng - 0.012) + "],[" + baseLat + "," + baseLng + "]]"
        );
        r101.addStop(new BusStop("S1", "Central Station", baseLat, baseLng));
        r101.addStop(new BusStop("S2", "Tech Park", baseLat + 0.015, baseLng + 0.012));
        r101.addStop(new BusStop("S3", "Art Museum", baseLat + 0.02, baseLng + 0.005));
        r101.addStop(new BusStop("S4", "West End", baseLat + 0.008, baseLng - 0.012));
        routeRepository.save(r101);

        // --- Route 2: R202 Airport Express ---
        BusRoute r202 = new BusRoute(
                "R202",
                "202 Airport Express",
                "Express",
                "#10b981",
                "[[" + (baseLat - 0.01) + "," + (baseLng - 0.01) + "],[" + (baseLat - 0.005) + "," + baseLng + "],[" + baseLat + "," + (baseLng + 0.02) + "],[" + (baseLat - 0.01) + "," + (baseLng + 0.04) + "],[" + (baseLat - 0.02) + "," + (baseLng + 0.06) + "]]"
        );
        r202.addStop(new BusStop("S5", "South Terminal", baseLat - 0.01, baseLng - 0.01));
        r202.addStop(new BusStop("S6", "Financial District", baseLat - 0.005, baseLng));
        r202.addStop(new BusStop("S7", "East Side Hub", baseLat, baseLng + 0.02));
        r202.addStop(new BusStop("S8", "International Airport", baseLat - 0.02, baseLng + 0.06));
        routeRepository.save(r202);

        // --- Route 3: R303 Metro Commuter ---
        BusRoute r303 = new BusRoute(
                "R303",
                "303 Metro Commuter",
                "Regular",
                "#f59e0b",
                "[[" + (baseLat + 0.03) + "," + (baseLng - 0.03) + "],[" + (baseLat + 0.02) + "," + (baseLng - 0.02) + "],[" + (baseLat + 0.01) + "," + (baseLng - 0.01) + "],[" + baseLat + "," + baseLng + "],[" + (baseLat - 0.02) + "," + (baseLng + 0.01) + "],[" + (baseLat - 0.03) + "," + (baseLng + 0.02) + "]]"
        );
        r303.addStop(new BusStop("S9", "North Suburbs", baseLat + 0.03, baseLng - 0.03));
        r303.addStop(new BusStop("S10", "University Campus", baseLat + 0.02, baseLng - 0.02));
        r303.addStop(new BusStop("S11", "Central Station", baseLat, baseLng));
        r303.addStop(new BusStop("S12", "South Industrial", baseLat - 0.03, baseLng + 0.02));
        routeRepository.save(r303);

        // --- Initial Buses ---
        Bus b001 = new Bus("B001", "R101", "CX-1024", "Alex Johnson", 60, 42, baseLat, baseLng, 35, 45, "S2", "on_time", 0);
        Bus b002 = new Bus("B002", "R101", "CX-1025", "Maria Garcia", 60, 55, baseLat + 0.02, baseLng + 0.005, 40, 220, "S4", "delayed", 300);
        Bus b003 = new Bus("B003", "R202", "AX-9900", "David Lee", 40, 12, baseLat - 0.005, baseLng, 65, 90, "S7", "on_time", 0);
        Bus b004 = new Bus("B004", "R303", "MC-5544", "Sarah Smith", 80, 75, baseLat + 0.02, baseLng - 0.02, 25, 135, "S11", "delayed", 120);

        busRepository.saveAll(Arrays.asList(b001, b002, b003, b004));

        System.out.println(">>> Bus Tracking System Database Initialized with " + routeRepository.count() + " routes & " + busRepository.count() + " buses.");
    }
}
