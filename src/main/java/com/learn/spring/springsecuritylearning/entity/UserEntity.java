package com.learn.spring.springsecuritylearning.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String fullName;
    private String email;
    @Column(length = 60)
    private String password;
    private boolean enabled = false;
}
