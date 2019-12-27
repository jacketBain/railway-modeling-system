package com.railwaymodelingsystem.service;

import com.railwaymodelingsystem.model.rms.Block;
import com.railwaymodelingsystem.model.rms.Link;

import java.util.List;

public interface LinkService {
    List<Link> getLinksByBlockFrom(Block blockFrom);
    List<Link> getByBlockFromAndBlockTo(Block blockFrom, Block blockTo);
    List<Link> getByBlockFromOrBlockTo(Block blockFrom, Block blockTo);
    List<Link> getBlocksByBlockTo(Block blockTo);
    Link addLink(Link link);
    void deleteLink(Link link);
}
