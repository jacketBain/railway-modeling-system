package com.railwaymodelingsystem.service;

import com.railwaymodelingsystem.model.rms.Block;
import com.railwaymodelingsystem.model.rms.Link;

import java.util.List;

public interface LinkService {
    List<Link> getLinksByBlockFrom(Block blockFrom);
    Boolean getByBlockFromAndAndBlockTo(Block blockFrom, Block blockTo);
//    Link addLink(Link link);
}
