package com.railwaymodelingsystem.controller;

import com.railwaymodelingsystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    final
    UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/login")
    public String loginPage(Model model){
//        model.addAttribute("title", "Welcome to Railway Modeling System");
//        /*TODO
//         *  1. Передатть текст хедера
//         *  2. Передать картинку*/

        return "login";
    }
}
