package com.example.authenticationdemo.model.dto;

import com.example.authenticationdemo.model.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link com.example.authenticationdemo.model.User} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private  UUID userId;
    private  String userName;
    private  String password;
    private Roles role;
}