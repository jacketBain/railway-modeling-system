package com.railwaymodelingsystem.service.impl;

import com.railwaymodelingsystem.model.rms.Block;
import com.railwaymodelingsystem.model.rms.Link;
import com.railwaymodelingsystem.repository.LinkRepository;
import com.railwaymodelingsystem.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkRepository linkRepository;

    @Override
    public List<Link> getLinksByBlockFrom(Block blockFrom) {
        return linkRepository.getLinksByBlockFrom(blockFrom);
    }

    @Override
    public Boolean getByBlockFromAndAndBlockTo(Block blockFrom, Block blockTo) {
        return linkRepository.getByBlockFromAndAndBlockTo(blockFrom, blockTo) != null;
    }

//    @Override
//    public Link addLink(Link link) {
//        return linkRepository.saveAndFlush(link);
//    }
}
