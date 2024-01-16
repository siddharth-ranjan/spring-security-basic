package com.learn.spring.springsecuritylearning.controller;

import com.learn.spring.springsecuritylearning.entity.UserEntity;
import com.learn.spring.springsecuritylearning.model.UserModel;
import com.learn.spring.springsecuritylearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel){
        UserEntity user = userService.registerUser(userModel);
        return "Success";
    }
}
