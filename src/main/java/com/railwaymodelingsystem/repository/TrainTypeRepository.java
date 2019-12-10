package com.railwaymodelingsystem.repository;

import com.railwaymodelingsystem.model.rms.TrainType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainTypeRepository extends JpaRepository<TrainType, String> {
}
