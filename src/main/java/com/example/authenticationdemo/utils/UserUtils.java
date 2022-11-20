package com.example.authenticationdemo.utils;

import com.example.authenticationdemo.service.impl.UserDetailsImpl;
import com.example.authenticationdemo.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class UserUtils {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    public UserDetailsImpl getUser(String header) {
        String userName;
        try {
            userName = jwtUtils.getUserName(header);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token解析失败");
        }
        return (UserDetailsImpl) userDetailsService.loadUserByUsername(userName);
    }

    public boolean isAllowed(UserDetailsImpl user, UUID id) {
        String authority = user.getUser().getRole().name();
        boolean highAuth = authority.equals("ADMIN");
        boolean isSameUser = id.equals(user.getUser().getId());
        return highAuth || isSameUser;
    }

    public boolean isAdmin(UserDetailsImpl user) {
        String authority = user.getUser().getRole().name();
        return authority.equals("ADMIN");
    }


}
