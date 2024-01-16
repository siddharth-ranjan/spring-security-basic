package com.learn.spring.springsecuritylearning.service;

import com.learn.spring.springsecuritylearning.entity.UserEntity;
import com.learn.spring.springsecuritylearning.model.UserModel;

public interface UserService {
    UserEntity registerUser(UserModel userModel);
}
