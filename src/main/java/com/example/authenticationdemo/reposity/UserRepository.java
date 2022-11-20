package com.example.authenticationdemo.reposity;

import com.example.authenticationdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUserName(String name);

    Optional<User> findUserByUserName(String username);

    void deleteUserByUserName(String userName);
}