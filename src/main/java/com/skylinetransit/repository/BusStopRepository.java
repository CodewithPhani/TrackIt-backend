package com.skylinetransit.repository;

import com.skylinetransit.model.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BusStopRepository extends JpaRepository<BusStop, String> {
    List<BusStop> findByRouteId(String routeId);
}
