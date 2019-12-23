package com.railwaymodelingsystem.repository;

import com.railwaymodelingsystem.model.rms.Shedule;
import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.model.rms.compositeKey.ShedulePrimary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SheduleRepository extends JpaRepository<Shedule, ShedulePrimary> {
    List<Shedule> getAllByStation(Station station);
    List<Shedule> getByKey(ShedulePrimary shedulePrimary);
    void deleteByKey(ShedulePrimary shedulePrimary);
}
