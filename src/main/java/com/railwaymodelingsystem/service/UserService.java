package com.railwaymodelingsystem.service;

import com.railwaymodelingsystem.model.UserEntity;

public interface UserService {
    UserEntity addUser(UserEntity userEntity);
    UserEntity getByName(String name);
}
