package com.skylinetransit.repository;

import com.skylinetransit.model.BusRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<BusRoute, String> {
}
