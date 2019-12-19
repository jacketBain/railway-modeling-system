package com.railwaymodelingsystem.service.impl;

import com.railwaymodelingsystem.model.User;
import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.repository.StationRepository;
import com.railwaymodelingsystem.service.StationService;
import org.springframework.stereotype.Service;

@Service
public class StationServiceImpl implements StationService {
    private final StationRepository stationRepository;

    public StationServiceImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public Station addStation(Station station) {
        return stationRepository.saveAndFlush(station);
    }

    @Override
    public Boolean isExists(String station, User user) {
       return stationRepository.existsByNameAndUser(station, user);
    }

    @Override
    public Station getStationByNameAndUser(String name, User user) {
        return stationRepository.findByNameAndUser(name, user);
    }

    @Override
    public void deleteByNameAndUser(String name, User user) {
        stationRepository.deleteByNameAndUser(name, user);
    }
}
