package com.railwaymodelingsystem.service;

import com.railwaymodelingsystem.model.rms.TrainType;

import java.util.List;

public interface TrainTypeService {
    TrainType getTrainType(String type);
    List<TrainType> getAllTypes();
}
