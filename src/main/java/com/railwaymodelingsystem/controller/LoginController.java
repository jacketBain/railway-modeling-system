package com.railwaymodelingsystem.controller;

import com.railwaymodelingsystem.model.User;
import com.railwaymodelingsystem.model.rms.City;
import com.railwaymodelingsystem.service.CityService;
import com.railwaymodelingsystem.service.UserService;
import com.railwaymodelingsystem.utils.EncryptedPasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CityService cityService;

    @GetMapping(value = "/login")
    public String loginPage(Model model){
//        model.addAttribute("title", "Welcome to Railway Modeling System");
//        /*TODO
//         *  1. Передатть текст хедера
//         *  2. Передать картинку*/
        User  user = new User();
        user.setUsername("admin");
        user.setPassword(EncryptedPasswordUtils.encryptePassword("12345678"));
        userService.addUser(user);

        City city = new City();
        city.setName("Самара");
        cityService.addCity(city);

        return "login";
    }
}
