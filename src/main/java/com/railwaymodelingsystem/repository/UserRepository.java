package com.railwaymodelingsystem.repository;

import com.railwaymodelingsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,String> {
    @Query("select u from User u where u.username = :name")
    User findByName(@Param("name")String name);
}
