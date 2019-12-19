package com.railwaymodelingsystem.repository;

import com.railwaymodelingsystem.model.rms.Block;
import com.railwaymodelingsystem.model.rms.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link,String> {
//    Link addLink(Link link);
    List<Link> getLinksByBlockFrom(Block blockFrom);
    Boolean getByBlockFromAndAndBlockTo(Block blockFrom, Block blockTo);
}
