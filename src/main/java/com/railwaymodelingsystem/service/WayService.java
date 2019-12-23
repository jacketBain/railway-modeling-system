package com.railwaymodelingsystem.service;

import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.model.rms.Way;

import java.util.List;

public interface WayService {
    Way addWay(Way way);
    List<Way> getByWayAndStation(Integer number, Station station);
    void removeWay(Way way);
    Way editWay(Way way);
    List<Way> getByStation(Station station);
}
