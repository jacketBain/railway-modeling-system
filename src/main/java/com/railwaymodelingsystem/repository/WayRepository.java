package com.railwaymodelingsystem.repository;

import com.railwaymodelingsystem.model.rms.Way;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WayRepository extends JpaRepository<Way,Integer> {
}
