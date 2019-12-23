package com.railwaymodelingsystem.repository;

import com.railwaymodelingsystem.model.User;
import com.railwaymodelingsystem.model.rms.Block;
import com.railwaymodelingsystem.model.rms.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Integer> {

    Boolean existsByNameAndUser(String name, User user);
    Station findByName(String name);
    Station findByNameAndUser(String name, User user);
    void deleteByNameAndUser(String name, User user);
}
