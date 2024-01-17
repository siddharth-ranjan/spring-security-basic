package com.learn.spring.springsecuritylearning.controller;

import com.learn.spring.springsecuritylearning.entity.UserEntity;
import com.learn.spring.springsecuritylearning.event.RegistrationCompleteEvent;
import com.learn.spring.springsecuritylearning.model.UserModel;
import com.learn.spring.springsecuritylearning.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest httpServletRequest){
        UserEntity user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(httpServletRequest)
        ));
        return "Success";
    }

    @GetMapping("/hello")
    public String getHello(){
        return "Success";
    }

    @PostMapping("/testPost")
    public String testPost(@RequestBody String str){
        return str;
    }

    private String applicationUrl(HttpServletRequest httpServletRequest) {
        return "http://"
                + httpServletRequest.getServerName()
                + ":"
                + httpServletRequest.getServerPort()
                + httpServletRequest.getContextPath();
    }
}
