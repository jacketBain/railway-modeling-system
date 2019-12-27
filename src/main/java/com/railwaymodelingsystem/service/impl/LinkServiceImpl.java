package com.railwaymodelingsystem.service.impl;

import com.railwaymodelingsystem.model.rms.Block;
import com.railwaymodelingsystem.model.rms.Link;
import com.railwaymodelingsystem.repository.LinkRepository;
import com.railwaymodelingsystem.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkRepository linkRepository;

    @Override
    public List<Link> getLinksByBlockFrom(Block blockFrom) {
        return linkRepository.getLinksByBlockFrom(blockFrom);
    }

    @Override
    public List<Link> getByBlockFromAndBlockTo(Block blockFrom, Block blockTo) {
        return linkRepository.getByBlockFromAndBlockTo(blockFrom, blockTo);
    }

    @Override
    public List<Link> getByBlockFromOrBlockTo(Block blockFrom, Block blockTo) {
        return linkRepository.getByBlockFromOrBlockTo(blockFrom, blockTo);
    }

    @Override
    public List<Link> getBlocksByBlockTo(Block blockTo) {
        return linkRepository.getLinksByBlockTo(blockTo);
    }

    @Override
    public Link addLink(Link link) {
        return linkRepository.saveAndFlush(link);
    }

    @Override
    public void deleteLink(Link link) {
        linkRepository.delete(link);
        linkRepository.flush();
    }

}
