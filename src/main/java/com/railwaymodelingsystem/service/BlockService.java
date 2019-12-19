package com.railwaymodelingsystem.service;

import com.railwaymodelingsystem.model.rms.Block;
import com.railwaymodelingsystem.model.rms.Station;

import java.util.List;

public interface BlockService {
    Block addBlock(Block block);
    Block findByNameAndStation(String name, Station station);
}
