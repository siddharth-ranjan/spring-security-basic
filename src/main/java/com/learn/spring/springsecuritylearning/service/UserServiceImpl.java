package com.learn.spring.springsecuritylearning.service;

import com.learn.spring.springsecuritylearning.entity.PasswordToken;
import com.learn.spring.springsecuritylearning.entity.UserEntity;
import com.learn.spring.springsecuritylearning.entity.VerificationToken;
import com.learn.spring.springsecuritylearning.model.UserModel;
import com.learn.spring.springsecuritylearning.repository.PasswordTokenRepository;
import com.learn.spring.springsecuritylearning.repository.UserRepository;
import com.learn.spring.springsecuritylearning.repository.VerificationTokenRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Override
    public UserEntity registerUser(UserModel userModel) {
        UserEntity user = new UserEntity();
        user.setFullName(userModel.getFullName());
        user.setEmail(userModel.getEmail());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));

        userRepository.save(user);

        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, UserEntity user) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if(verificationToken == null){
            return "invalid";
        }

        UserEntity user = verificationToken.getUser();
        LocalDateTime loc = LocalDateTime.now();

        if((verificationToken.getExpirationTime().isBefore(loc))){
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }

        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public boolean checkIfEnabledUserIsPresent(UserModel userModel) {
         return userRepository.existsByEmailAndEnabledIsTrue(userModel.getEmail());
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public PasswordToken generateForgotPasswordToken(UserEntity user) {
        PasswordToken passwordToken = new PasswordToken(UUID.randomUUID().toString(), user);
        passwordTokenRepository.save(passwordToken);
        return passwordToken;
    }

    @Override
    public UserEntity validateForgotPasswordToken(String token) {
        System.out.println("Correct till here");

        PasswordToken passwordToken = passwordTokenRepository.findByToken(token);
        UserEntity user = passwordToken.getUser();

        if(user == null){
            return null;
        }

        user.setPassword("2");
        userRepository.save(user);
        passwordTokenRepository.delete(passwordToken);

        return user;
    }

    @Override
    public UserEntity changePassword(String newPassword, String oldPassword, UserEntity user) {
        if(user.getPassword().matches(oldPassword)){
            user.setPassword(newPassword);
            userRepository.save(user);
            return user;
        }
        return null;
    }
}
