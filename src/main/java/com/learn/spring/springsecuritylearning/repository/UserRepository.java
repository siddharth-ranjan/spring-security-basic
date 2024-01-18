package com.learn.spring.springsecuritylearning.repository;

import com.learn.spring.springsecuritylearning.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
//    UserEntity findByEmail(String email);

    boolean existsByEmailAndEnabledIsTrue(String email);

}
