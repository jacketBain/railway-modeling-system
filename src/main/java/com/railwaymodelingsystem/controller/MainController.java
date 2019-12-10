package com.railwaymodelingsystem.controller;

import com.railwaymodelingsystem.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    final UserServiceImpl userService;

    public MainController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/home")
    public String mainPage(){
        return "home";
    }
}
