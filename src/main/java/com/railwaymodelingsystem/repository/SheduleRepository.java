package com.railwaymodelingsystem.repository;

import com.railwaymodelingsystem.model.rms.Shedule;
import com.railwaymodelingsystem.model.rms.compositeKey.ShedulePrimary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SheduleRepository extends JpaRepository<Shedule, ShedulePrimary> {
}
