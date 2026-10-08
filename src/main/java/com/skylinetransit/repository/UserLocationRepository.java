package com.skylinetransit.repository;

import com.skylinetransit.model.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {
    Optional<UserLocation> findTopBySessionIdOrderByTimestampDesc(String sessionId);
}
