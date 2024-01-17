package com.learn.spring.springsecuritylearning.event.listener;

import com.learn.spring.springsecuritylearning.entity.UserEntity;
import com.learn.spring.springsecuritylearning.event.RegistrationCompleteEvent;
import com.learn.spring.springsecuritylearning.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //create verification token for the user
        UserEntity user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);
        //send mail to user
        String url =
                event.getApplicationUrl()
                    + "/verifyRegistration?token="
                    + token;

        log.info("Click the link to verify your acc: {}", url);
    }
}
