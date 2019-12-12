package com.railwaymodelingsystem.repository;

import com.railwaymodelingsystem.model.User;
import com.railwaymodelingsystem.model.rms.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Integer> {

    Boolean existsByNameAndUser(String name, User user);
    Station findByName(String name);
}
