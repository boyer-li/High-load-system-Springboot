package com.example.authenticationdemo.Filter;

import com.example.authenticationdemo.reposity.UserRepository;
import com.example.authenticationdemo.service.impl.UserDetailsImpl;
import com.example.authenticationdemo.service.impl.UserDetailsServiceImpl;
import com.example.authenticationdemo.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //get token from request
        String token = request.getHeader("token");

        if (!StringUtils.hasText(token)) {
            //let it go
            filterChain.doFilter(request, response);
            return;
        }

        //analysis token
        String userName;
        try {
            userName = jwtUtils.getUserName(token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token is invalid");
        }

        UserDetailsImpl user = (UserDetailsImpl) userDetailsService.loadUserByUsername(userName);


        if (Objects.isNull(user)) {
            throw new RuntimeException("user is not login");
        }

        //validate token is Expired?
        if (!jwtUtils.validateJwtToken(token)) {
            throw new RuntimeException("token is expired");
        }else {
            token  =  jwtUtils.refreshToken(token);
            response.setHeader("refresh_token", token);
        }

        //store in SecurityContextHolder
        // TODO: 10/19/2022 获取权限信息封装到Authentiacation
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
