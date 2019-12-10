package com.railwaymodelingsystem.service.impl;

import com.railwaymodelingsystem.model.User;
import com.railwaymodelingsystem.repository.UserRepository;
import com.railwaymodelingsystem.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User getByName(String name) {
        return userRepository.findByName(name);
    }
}
