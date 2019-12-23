package com.railwaymodelingsystem.repository;

import com.railwaymodelingsystem.model.rms.Block;
import com.railwaymodelingsystem.model.rms.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link,String> {
    List<Link> getLinksByBlockFrom(Block blockFrom);
    List<Link> getByBlockFromAndBlockTo(Block blockFrom, Block blockTo);
    List<Link> getByBlockFromOrBlockTo(Block blockFrom, Block blockTo);
}
