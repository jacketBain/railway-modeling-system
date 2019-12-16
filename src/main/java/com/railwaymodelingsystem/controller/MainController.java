package com.railwaymodelingsystem.controller;

import com.railwaymodelingsystem.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class MainController {

    final UserServiceImpl userService;

    public MainController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String loginPage(Model model, Principal principal){
        if(principal == null)
            return "login";
        else{
            model.addAttribute("user", principal.getName());
            return "home";
        }

    }

    @GetMapping("/home")
    public String mainPage(Model model, Principal principal){
        model.addAttribute("user", principal.getName());
        return "home";
    }

    @GetMapping("/logout-success")
    public String logoutSuccess(){
        return "login";
    }
}
