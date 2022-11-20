package com.example.authenticationdemo.model;


import com.example.authenticationdemo.model.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

import static com.example.authenticationdemo.model.DbConstants.DB_SCHEMA;


@Entity
@Table(name = "users", schema = DB_SCHEMA)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "user_name",updatable = false)
    private String userName;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private Roles role;

}
