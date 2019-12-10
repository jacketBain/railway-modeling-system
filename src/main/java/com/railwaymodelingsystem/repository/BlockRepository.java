package com.railwaymodelingsystem.repository;

import com.railwaymodelingsystem.model.rms.Block;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block,Integer> {
}
