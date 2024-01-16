package com.learn.spring.springsecuritylearning.service;

import com.learn.spring.springsecuritylearning.entity.UserEntity;
import com.learn.spring.springsecuritylearning.model.UserModel;
import com.learn.spring.springsecuritylearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity registerUser(UserModel userModel) {
        UserEntity user = new UserEntity();
        user.setFullName(userModel.getFullName());
        user.setEmail(userModel.getEmail());
        user.setPassword(userModel.getPassword());
        return null;
    }
}
