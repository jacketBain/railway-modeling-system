package com.railwaymodelingsystem.service.impl;

import com.railwaymodelingsystem.model.rms.Shedule;
import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.repository.SheduleRepository;
import com.railwaymodelingsystem.service.SheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SheduleServiceImpl implements SheduleService {
    @Autowired
    private SheduleRepository sheduleRepository;

    @Override
    public Shedule addShedule(Shedule shedule) {
        return sheduleRepository.saveAndFlush(shedule);
    }

    @Override
    public List<Shedule> getShedulesByStation(Station station) {
        return sheduleRepository.getAllByStation(station);
    }
}
