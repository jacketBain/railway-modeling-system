package com.railwaymodelingsystem.service;

import com.railwaymodelingsystem.model.rms.Block;

import java.util.List;

public interface BlockService {
    Block addBlock(Block block);
    Block getById(Block block);
    void delete(Integer id);
    Block editBlock(Block block);
    List<Block> getAll();
}
