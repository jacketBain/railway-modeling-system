package com.railwaymodelingsystem.service.impl;

import com.railwaymodelingsystem.model.UserEntity;
import com.railwaymodelingsystem.repository.UserRepository;
import com.railwaymodelingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity addUser(UserEntity userEntity) {
        return userRepository.saveAndFlush(userEntity);
    }

    @Override
    public UserEntity getByName(String name) {
        return userRepository.findByName(name);
    }
}
