package com.example.authenticationdemo;

import com.example.authenticationdemo.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthenticationdemoApplicationTests {

    @Autowired
    JwtUtils jwtUtils;

    @Test
    void contextLoads() {
    }

//    @Test
//    void test() {
//        log.info(jwtUtils.generateToken("spring-security-learn-how-it-works-with-spring-boot-with-cryptographic-exceptions"));
//    }

}
