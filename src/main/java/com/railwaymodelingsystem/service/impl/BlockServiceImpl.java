package com.railwaymodelingsystem.service.impl;

import com.railwaymodelingsystem.model.rms.Block;
import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.repository.BlockRepository;
import com.railwaymodelingsystem.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockServiceImpl implements BlockService {
    @Autowired
    BlockRepository blockRepository;

    @Override
    public Block addBlock(Block block) {
        return blockRepository.saveAndFlush(block);
    }

    @Override
    public Block findByNameAndStation(String name, Station station) {
        return blockRepository.findByNameAndStation(name, station);
    }
}
