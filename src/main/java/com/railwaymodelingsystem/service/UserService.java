package com.railwaymodelingsystem.service;

import com.railwaymodelingsystem.model.User;

public interface UserService {
    User addUser(User user);
    User getByName(String name);
}
