package com.railwaymodelingsystem.service.impl;

import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.repository.StationRepository;
import com.railwaymodelingsystem.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationServiceImpl implements StationService {
    @Autowired
    private StationRepository stationRepository;

    @Override
    public Station addStation(Station station) {
        return stationRepository.saveAndFlush(station);
    }
}
