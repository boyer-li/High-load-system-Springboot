package com.example.authenticationdemo.controller;

import com.example.authenticationdemo.model.dto.UserDto;
import com.example.authenticationdemo.service.impl.UserDetailsImpl;
import com.example.authenticationdemo.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;


    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@RequestBody UserDto userDto){
        log.info("access login: "+userDto);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        //auth failed
        if(Objects.isNull(authentication)){
            throw new RuntimeException("auth failed");
        }
        //auth access
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);
        return ResponseEntity.status(200).body(jwt);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(){
        //获取SercutityContext Holder中的用户Id
       UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
       UserDetailsImpl userDetails = (UserDetailsImpl) authenticationToken.getPrincipal();
         UUID uuid = userDetails.getUser().getId();
        return ResponseEntity.status(200).build();
    }
}
