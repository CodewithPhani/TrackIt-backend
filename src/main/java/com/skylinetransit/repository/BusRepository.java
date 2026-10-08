package com.skylinetransit.repository;

import com.skylinetransit.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, String> {
    List<Bus> findByRouteId(String routeId);
    List<Bus> findByStatus(String status);
}
