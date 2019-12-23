package com.railwaymodelingsystem.service.impl;

import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.model.rms.Way;
import com.railwaymodelingsystem.repository.WayRepository;
import com.railwaymodelingsystem.service.WayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WayServiceImpl implements WayService {

    @Autowired
    private WayRepository wayRepository;

    @Override
    public Way addWay(Way way) {
        return wayRepository.saveAndFlush(way);
    }

    @Override
    public List<Way> getByWayAndStation(Integer number, Station station) {
        return wayRepository.findByNumberAndStation(number, station);
    }

    @Override
    public void removeWay(Way way) {
        wayRepository.delete(way);
        wayRepository.flush();
    }

    @Override
    public Way editWay(Way way) {
        return null;
    }

    @Override
    public List<Way> getByStation(Station station) {
        return wayRepository.findAllByStation(station);
    }
}
