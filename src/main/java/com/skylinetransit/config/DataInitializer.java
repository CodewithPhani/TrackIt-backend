package com.skylinetransit.config;

import com.skylinetransit.model.Bus;
import com.skylinetransit.model.BusRoute;
import com.skylinetransit.model.BusStop;
import com.skylinetransit.repository.BusRepository;
import com.skylinetransit.repository.RouteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/** Demo seed data for the Anurag University shuttle route. */
@Component
public class DataInitializer implements CommandLineRunner {
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;

    public DataInitializer(RouteRepository routeRepository, BusRepository busRepository) {
        this.routeRepository = routeRepository;
        this.busRepository = busRepository;
    }

    @Override
    public void run(String... args) {
        double[][] points = {
            {17.477110, 78.563410}, // Radhika Bus Stop
            {17.485500, 78.574500}, // Saket
            {17.501800, 78.589000}, // Dammaiguda X Roads
            {17.499562, 78.596045}, // PS Rao Nagar
            {17.493360, 78.595770}, // Dubai Building
            {17.4193102, 78.6565403} // Anurag University
        };
        String pathJson = "[[17.47711,78.56341],[17.4855,78.5745],[17.5018,78.589],[17.499562,78.596045],[17.49336,78.59577],[17.4193102,78.6565403]]";
        if (!routeRepository.existsById("R101")) {
        BusRoute route = new BusRoute("R101", "Radhika - Anurag University", "Regular", "#3b82f6", pathJson);
        String[] names = {"Radhika Bus Stop", "Saket", "Dammaiguda X Roads", "PS Rao Nagar", "Dubai Building", "Anurag University"};
        String[] stopIds = {"S1", "S2", "S3", "S4", "S5", "S6"};
        for (int i = 0; i < points.length; i++) {
            route.addStop(new BusStop(stopIds[i], names[i], points[i][0], points[i][1]));
        }
        routeRepository.save(route);
        }

        // Additional pickup corridors. Coordinates are approximate demo stop pins; verify with transport staff before production.
        if (!routeRepository.existsById("R102")) {
        BusRoute route2 = new BusRoute("R102", "Nagaram - ECIL - Anurag University", "Regular", "#10b981",
            "[[17.4709,78.5794],[17.4778,78.5630],[17.4761,78.5590],[17.4740,78.5480],[17.4658,78.5485],[17.4193102,78.6565403]]");
        route2.addStop(new BusStop("S07", "Nagaram X Roads", 17.4709, 78.5794));
        route2.addStop(new BusStop("S08", "Radhika Theatre Junction", 17.4778, 78.5630));
        route2.addStop(new BusStop("S09", "ECIL X Roads", 17.4761, 78.5590));
        route2.addStop(new BusStop("S10", "AS Rao Nagar", 17.4740, 78.5480));
        route2.addStop(new BusStop("S11", "Sainikpuri Main Road", 17.4658, 78.5485));
        route2.addStop(new BusStop("S12", "Anurag University", 17.4193102, 78.6565403));
        routeRepository.save(route2);
        }

        if (!routeRepository.existsById("R103")) {
        BusRoute route3 = new BusRoute("R103", "Uppal - Boduppal - Anurag University", "Regular", "#f59e0b",
            "[[17.4018,78.5590],[17.4075,78.5688],[17.4135,78.5780],[17.4200,78.5900],[17.4308,78.6025],[17.4193102,78.6565403]]");
        route3.addStop(new BusStop("S13", "Uppal Metro Station", 17.4018, 78.5590));
        route3.addStop(new BusStop("S14", "Boduppal Main Road", 17.4075, 78.5688));
        route3.addStop(new BusStop("S15", "Peerzadiguda Junction", 17.4135, 78.5780));
        route3.addStop(new BusStop("S16", "Medipally X Roads", 17.4200, 78.5900));
        route3.addStop(new BusStop("S17", "Ghatkesar Road Junction", 17.4308, 78.6025));
        route3.addStop(new BusStop("S18", "Anurag University", 17.4193102, 78.6565403));
        routeRepository.save(route3);
        }

        Bus b001 = new Bus("B001", "R101", "TS-08-1024", "Ravi Kumar", 60, 42, points[0][0], points[0][1], 0, 90, "S2", "on_time", 0);
        Bus b002 = new Bus("B002", "R101", "TS-08-1025", "Suresh Reddy", 60, 48, points[1][0], points[1][1], 25, 110, "S3", "on_time", 0);
        Bus b003 = new Bus("B003", "R101", "TS-08-9900", "Prakash Rao", 50, 35, points[2][0], points[2][1], 30, 120, "S4", "on_time", 0);
        Bus b004 = new Bus("B004", "R101", "TS-08-5544", "Mahesh Kumar", 80, 65, points[3][0], points[3][1], 0, 125, "S5", "delayed", 120);
        Bus b005 = new Bus("B005", "R101", "TS-08-2205", "Kiran Reddy", 50, 28, points[4][0], points[4][1], 20, 180, "S6", "on_time", 0);
        Bus b006 = new Bus("B006", "R102", "TS-08-6606", "Driver To Be Assigned", 55, 0, 17.4709, 78.5794, 0, 90, "S08", "stopped", 0);
        Bus b007 = new Bus("B007", "R103", "TS-08-7707", "Driver To Be Assigned", 55, 0, 17.4018, 78.5590, 0, 90, "S14", "stopped", 0);
        for (Bus bus : Arrays.asList(b001, b002, b003, b004, b005, b006, b007)) {
            if (!busRepository.existsById(bus.getId())) busRepository.save(bus);
        }
        System.out.println(">>> Anurag University bus routes initialized: " + routeRepository.count() + " route(s), " + busRepository.count() + " buses.");
    }
}
