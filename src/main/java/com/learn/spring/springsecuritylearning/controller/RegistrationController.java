package com.learn.spring.springsecuritylearning.controller;

import com.learn.spring.springsecuritylearning.entity.UserEntity;
import com.learn.spring.springsecuritylearning.event.RegistrationCompleteEvent;
import com.learn.spring.springsecuritylearning.model.UserModel;
import com.learn.spring.springsecuritylearning.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest httpServletRequest){
        if(userService.checkIfEnabledUserIsPresent(userModel)){
            return "User already present";
        }
        try{
            UserEntity user = userService.registerUser(userModel);
            publisher.publishEvent(new RegistrationCompleteEvent(
                    user,
                    applicationUrl(httpServletRequest)
            ));
            return "Success";
        }catch(DataIntegrityViolationException e){
            return "Email already present, Please verify from the mail.";
        }

    }

    @GetMapping("/hello")
    public String getHello(){
        return "Success";
    }

    @PostMapping("/testPost")
    public String testPost(@RequestBody String str){
        return str;
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "User verified successfully";
        }
        else{
            return "Bad request";
        }
    }


//    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request){
//        VerificationToken verificationToken =
//                userService.generateNewVerificationToken(oldToken);
//
//        UserEntity user = verificationToken.getUser();
//        resendVerificationTokenMail(user, applicationUrl(request));
//        return "Verification Link Sent";
//    }

//    private void resendVerificationTokenMail(UserEntity user, String applicationUrl) {
//        String url =
//                applicationUrl
//                        + "/verifyRegistration?token="
//                        + token;
//
//        log.info("Click the link to verify your acc: {}", url);
//    }


    private String applicationUrl(HttpServletRequest httpServletRequest) {
        return "http://"
                + httpServletRequest.getServerName()
                + ":"
                + httpServletRequest.getServerPort()
                + httpServletRequest.getContextPath();
    }
}
