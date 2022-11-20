package com.example.authenticationdemo.service.impl;

import com.example.authenticationdemo.model.User;
import com.example.authenticationdemo.reposity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //find user by username
            User user =  userRepository.findUserByUserName(username).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        //if (Objects.isNull(user)) {
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("user not found");
        }
        return new UserDetailsImpl(user);
    }

}
