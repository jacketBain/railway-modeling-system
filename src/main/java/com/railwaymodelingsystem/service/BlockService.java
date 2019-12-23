package com.railwaymodelingsystem.service;

import com.railwaymodelingsystem.model.rms.Block;
import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.model.rms.Way;

import java.util.List;

public interface BlockService {
    Block addBlock(Block block);
    List<Block> findByNameAndStation(String name, Station station);
    List<Block> findByStationAndPlatform(Station station, Integer platformNumber);
    List<Block> findAllByStationAndWay(Station station, Way way);
    List<Block> findAllByStation(Station station);
    Block editBlock(Block block);
    void deleteBlock(Block block);
}
