package com.learn.spring.springsecuritylearning.controller;

import com.learn.spring.springsecuritylearning.entity.PasswordToken;
import com.learn.spring.springsecuritylearning.entity.UserEntity;
import com.learn.spring.springsecuritylearning.entity.VerificationToken;
import com.learn.spring.springsecuritylearning.event.RegistrationCompleteEvent;
import com.learn.spring.springsecuritylearning.model.PasswordModel;
import com.learn.spring.springsecuritylearning.model.UserModel;
import com.learn.spring.springsecuritylearning.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
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


    @PostMapping("/resendVerificationToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request){
        VerificationToken verificationToken =
                userService.generateNewVerificationToken(oldToken);

        resendVerificationTokenMail(applicationUrl(request), verificationToken.getToken());
        return "Verification Link Sent";
    }

    private void resendVerificationTokenMail(String applicationUrl, String token) {
        String url =
                applicationUrl
                        + "/verifyRegistration?token="
                        + token;

        log.info("Click the link to verify your acc: {}", url);
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request){
        UserEntity user = userService.getUserByEmail(passwordModel.getEmail());
        if(user == null){
            return "User not registered.";
        }

        PasswordToken passwordToken = userService.generateForgotPasswordToken(user);
        String url =
                applicationUrl(request)
                + "/verifyForgotPassword?token="
                + passwordToken.getToken();

        log.info("Click here to reset password {}", url);
        return "Success";
    }

    @PostMapping("/verifyForgotPassword")
    public String verifyForgotPassword(@RequestParam("token") String token){
        UserEntity user = userService.validateForgotPasswordToken(token);
        if(user == null){
            return "Bad Request";
        }
        log.info("Token received {}.", token);
        return "Password changed";
    }


    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel){
        UserEntity user = userService.getUserByEmail(passwordModel.getEmail());
        System.out.println("user = " + user);
        if(user == null){
            return "User not found";
        }
        user = userService.changePassword(passwordModel.getNewPassword(), passwordModel.getOldPassword(), user);

        return "Password Changed Successfully";
    }

//    public String generateResetPasswordToken(UserEntity user){
//        PasswordToken passwordToken = userService.generateResetPasswordToken();
//        return null;
//    }


    private String applicationUrl(HttpServletRequest httpServletRequest) {
        return "http://"
                + httpServletRequest.getServerName()
                + ":"
                + httpServletRequest.getServerPort()
                + httpServletRequest.getContextPath();
    }
}
