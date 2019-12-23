package com.railwaymodelingsystem.repository;

import com.railwaymodelingsystem.model.rms.Block;
import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.model.rms.Way;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlockRepository extends JpaRepository<Block,Integer> {
    List<Block> findByNameAndStation(String name, Station station);
    List<Block> findByStationAndPlatformNumber(Station station, Integer platformNumber);
    List<Block> findAllByStationAndWay(Station station, Way way);
    List<Block> findAllByStation(Station station);
}
