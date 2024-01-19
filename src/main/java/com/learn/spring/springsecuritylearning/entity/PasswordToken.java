package com.learn.spring.springsecuritylearning.entity;

import com.learn.spring.springsecuritylearning.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private static final int EXPIRATION_TIME = 10;

    private String token;

    @OneToOne()
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_RESET_PASSWORD_TOKEN")
    )
    private UserEntity user;

    private LocalDateTime expirationTime;

    public PasswordToken(String token, UserEntity user) {
        this.token = token;
        this.user = user;
        this.expirationTime = LocalDateTime.now().plusMinutes(EXPIRATION_TIME);
    }
}
