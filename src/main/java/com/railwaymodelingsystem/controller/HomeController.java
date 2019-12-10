package com.railwaymodelingsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/start_Constructor")
    public String startConstructor(){
        return "startConstructor";
    }
}
