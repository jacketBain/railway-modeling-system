package com.railwaymodelingsystem.controller;

import com.railwaymodelingsystem.model.User;
import com.railwaymodelingsystem.service.CityService;
import com.railwaymodelingsystem.service.UserService;
import com.railwaymodelingsystem.service.impl.CityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    CityService cityService;
    final
    UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/start_Constructor")
    public String startConstructor(Model model, Principal principal){
        User user = userService.getByName(principal.getName());
        model.addAttribute("stations", user.getStations());
        model.addAttribute("cityList", cityService.getAllCity());
        return "startConstructor";
    }

    @GetMapping("/start_Shedule")
    public String startShedule(Model model, Principal principal){
        User user = userService.getByName(principal.getName());
        model.addAttribute("stations", user.getStations());
        return "startShedule";
    }
}
