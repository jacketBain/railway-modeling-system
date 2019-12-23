package com.railwaymodelingsystem.service.impl;

import com.railwaymodelingsystem.model.rms.TrainType;
import com.railwaymodelingsystem.repository.TrainTypeRepository;
import com.railwaymodelingsystem.service.TrainTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainTypeImpl implements TrainTypeService {
    @Autowired
    TrainTypeRepository trainTypeRepository;
    @Override
    public TrainType getTrainType(String type) {
        return trainTypeRepository.findByName(type);
    }

    @Override
    public List<TrainType> getAllTypes() {
        return trainTypeRepository.findAll();
    }
}
