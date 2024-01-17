package com.learn.spring.springsecuritylearning.service;

import com.learn.spring.springsecuritylearning.entity.UserEntity;
import com.learn.spring.springsecuritylearning.model.UserModel;
import org.springframework.stereotype.Service;

//@Service
public interface UserService {
    UserEntity registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, UserEntity user);
}
