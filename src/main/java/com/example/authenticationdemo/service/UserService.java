package com.example.authenticationdemo.service;


import com.example.authenticationdemo.exception.UserNotFoundException;
import com.example.authenticationdemo.model.User;
import com.example.authenticationdemo.model.dto.UserDto;
import com.example.authenticationdemo.reposity.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder bCryptPasswordEncoder;
    private ModelMapper modelMapper;


    public Page<UserDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).map(user -> modelMapper.map(user, UserDto.class));
    }

    @Transactional
    @Modifying
    public void delete(String userName) {
        log.info("userName-delete:"+userName);
       userRepository.findUserByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(userName));
        userRepository.deleteUserByUserName(userName);
    }


    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public boolean register(UserDto userDto) {
        log.info("userDto:"+userDto);
        if (userRepository.existsByUserName(userDto.getUserName())) {
            return false;
        }
        User user = modelMapper.map(userDto, User.class);
        user.setId(UUID.randomUUID());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUserName(userDto.getUserName());
        log.info("register:"+user);
        userRepository.save(user);
        return true;
    }

    public boolean existsByUserId(UUID userId) {
        log.info("userId-exists:"+userId);
        return userRepository.existsById(userId);
    }

    @Transactional
    public void deleteById(UUID userId) {
        log.info("userId-delete:"+userId);
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.deleteById(userId);
    }
}
