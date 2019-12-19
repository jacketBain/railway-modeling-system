package com.railwaymodelingsystem.repository;

import com.railwaymodelingsystem.model.rms.Station;
import com.railwaymodelingsystem.model.rms.Way;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WayRepository extends JpaRepository<Way,Integer> {
    Way findByNumberAndStation(Integer number, Station station);
    List<Way> findAllByStation(Station station);
}
