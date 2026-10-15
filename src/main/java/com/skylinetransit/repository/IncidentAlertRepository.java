package com.skylinetransit.repository;

import com.skylinetransit.model.IncidentAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IncidentAlertRepository extends JpaRepository<IncidentAlert, Long> {
    List<IncidentAlert> findByBusIdOrderByTimestampDesc(String busId);
    List<IncidentAlert> findTop20ByOrderByTimestampDesc();
}
