package com.railwaymodelingsystem.controller;


import com.railwaymodelingsystem.model.AjaxResponseBody;
import com.railwaymodelingsystem.model.UserEntity;
import com.railwaymodelingsystem.service.UserService;
import com.railwaymodelingsystem.utils.EncryptedPasswordUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Controller
public class SignUpController {

    final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupPage(){
        return "signup";
    }

    @GetMapping("/signup/users")
    public ResponseEntity<AjaxResponseBody> checkUserExist(@RequestParam(value = "login") String login) {
        if (userService.getByName(login) != null) {
            return ResponseEntity.ok(new AjaxResponseBody("Пользователь "
                    + login + " уже существует", "ERROR"));
        }
        else{
            return ResponseEntity.ok(new AjaxResponseBody("Пользователя "
                    + login + " нет в системе", "SUCCESS"));
        }
    }

    @PostMapping(value = "/signup", produces = "application/json")
    public ResponseEntity<AjaxResponseBody> addUser(@NotNull @ModelAttribute UserEntity user){
        String password = EncryptedPasswordUtils.encryptePassword(user.getPassword());
        user.setPassword(password);
        userService.addUser(user);
        return ResponseEntity.ok(new AjaxResponseBody("Пользователь" + user.getUsername() + " зарегистрирован", "SUCCESS"));
    }
}
