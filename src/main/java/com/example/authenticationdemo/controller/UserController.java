package com.example.authenticationdemo.controller;

import com.example.authenticationdemo.model.dto.UserDto;
import com.example.authenticationdemo.service.UserService;
import com.example.authenticationdemo.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    @Value("${spring.data.web.pageable.default-page-size}")
    private final int defaultPageSize = 50;

    private final UserService userService;

    private final UserUtils userUtils;


    @GetMapping(value = "findAll",produces = "application/json")
    public ResponseEntity<Object> findAll(@RequestHeader("token") String token,
                                          @RequestParam(value = "page", defaultValue = "0") Integer page,
                                          @RequestParam(value = "size", required = false) Integer size) throws IOException {
        log.info("access findUsers");

        if (userUtils.isAdmin(userUtils.getUser(token))) {
            boolean isInfiniteScroll = size == null;
            Page<UserDto> userPage = userService.findAll(page, isInfiniteScroll ? defaultPageSize : size);
            if (userPage.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            if (!isInfiniteScroll) {
                return ResponseEntity.ok()
                        .header("x-total-count", String.valueOf(userPage.getTotalElements()))
                        .body(userPage.getContent());
            }
            return ResponseEntity.ok(userPage);
        }else {
            return ResponseEntity.badRequest().body("You are not admin");
        }
    }

    @PostMapping(value = "/register",produces = "application/json",consumes = "application/json")
    public ResponseEntity<Object> registerUser(@RequestBody UserDto userDto) throws IOException {
        log.info("access register");
        return userService.register(userDto) ?
                ResponseEntity.ok().body("Register successfully") :
                ResponseEntity.badRequest().body("Register failed");
    }

    @DeleteMapping(value = "/delete",consumes = "application/json",produces = "application/json")
    public ResponseEntity<Object> deleteUser(@RequestBody UserDto userDto) throws IOException {
        log.info("access deleteUser:"+userDto.getUserName());
        if(userService.existsByUserId(userDto.getUserId())){
            userService.delete(userDto.getUserName());
            return ResponseEntity.ok().body("Delete successfully");
     }
        return ResponseEntity.badRequest().body("Delete failed");
    }


}
