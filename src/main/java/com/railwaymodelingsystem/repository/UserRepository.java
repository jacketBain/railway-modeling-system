package com.railwaymodelingsystem.repository;

import com.railwaymodelingsystem.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity,String> {
    @Query("select u from UserEntity u where u.username = :name")
    UserEntity findByName(@Param("name")String name);
}
