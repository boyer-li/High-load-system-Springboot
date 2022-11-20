package com.example.authenticationdemo.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
@Scope("singleton")
@Slf4j
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;
    private Key key;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;


    @PostConstruct
    void postConstruct() {
        key = new SecretKeySpec(Base64.getDecoder().decode(jwtSecret), SignatureAlgorithm.HS256.getJcaName());
    }

    //get expiration time


    public String getJwtToken(String token) {
        if (isEmpty(token)) {
            return null;
        }
        return token;
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (RuntimeException e) {
            log.info(e.getMessage());
            //skip
        }
        return false;
    }

    public String getUserName(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }



    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    //refresh token
    public String refreshToken(String token) {
        return Jwts.builder()
                .setSubject(getUserName(token))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }



}
