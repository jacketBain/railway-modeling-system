package com.railwaymodelingsystem.service;

import com.railwaymodelingsystem.model.rms.Shedule;
import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.model.rms.compositeKey.ShedulePrimary;

import java.util.List;

public interface SheduleService {
    Shedule addShedule(Shedule shedule);
    List<Shedule> getShedulesByStation(Station station);
    List<Shedule> getByKey(ShedulePrimary shedulePrimary);
    void removeShedule(ShedulePrimary shedulePrimary);
}
