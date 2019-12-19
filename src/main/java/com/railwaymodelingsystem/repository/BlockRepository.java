package com.railwaymodelingsystem.repository;

import com.railwaymodelingsystem.model.rms.Block;
import com.railwaymodelingsystem.model.rms.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block,Integer> {
    Block findByNameAndStation(String name, Station station);
}
