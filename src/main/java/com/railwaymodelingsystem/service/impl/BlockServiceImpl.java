package com.railwaymodelingsystem.service.impl;

import com.railwaymodelingsystem.model.rms.Block;
import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.model.rms.Way;
import com.railwaymodelingsystem.repository.BlockRepository;
import com.railwaymodelingsystem.repository.WayRepository;
import com.railwaymodelingsystem.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockServiceImpl implements BlockService {
    @Autowired
    BlockRepository blockRepository;

    @Autowired
    WayRepository wayRepository;

    @Override
    public Block addBlock(Block block) {
        return blockRepository.saveAndFlush(block);
    }

    @Override
    public List<Block> findByNameAndStation(String name, Station station) {
        return blockRepository.findByNameAndStation(name, station);
    }

    @Override
    public List<Block> findByStationAndPlatform(Station station, Integer platformNumber) {
        return blockRepository.findByStationAndPlatformNumber(station, platformNumber);
    }

    @Override
    public List<Block> findAllByStationAndWay(Station station, Way way) {
        return blockRepository.findAllByStationAndWay(station, way);
    }

    @Override
    public List<Block> findAllByStation(Station station) {
        return blockRepository.findAllByStation(station);
    }

    @Override
    public Block editBlock(Block block) {
        return blockRepository.save(block);
    }

    @Override
    public void deleteBlock(Block block) {
        blockRepository.delete(block);
        blockRepository.flush();
    }
}
