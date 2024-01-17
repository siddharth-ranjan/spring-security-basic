package com.learn.spring.springsecuritylearning.service;

import com.learn.spring.springsecuritylearning.entity.UserEntity;
import com.learn.spring.springsecuritylearning.entity.VerificationToken;
import com.learn.spring.springsecuritylearning.model.UserModel;

//@Service
public interface UserService {
    UserEntity registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, UserEntity user);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);
}
