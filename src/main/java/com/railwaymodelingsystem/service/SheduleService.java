package com.railwaymodelingsystem.service;

import com.railwaymodelingsystem.model.rms.Shedule;
import com.railwaymodelingsystem.model.rms.Station;

import java.util.List;

public interface SheduleService {
    Shedule addShedule(Shedule shedule);
    List<Shedule> getShedulesByStation(Station station);
}
