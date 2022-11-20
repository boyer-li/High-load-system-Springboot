package com.example.authenticationdemo.controller;


import com.example.authenticationdemo.model.dto.ProductDto;
import com.example.authenticationdemo.service.ProductService;
import com.example.authenticationdemo.service.impl.UserDetailsImpl;
import com.example.authenticationdemo.utils.UserUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    @Value("${spring.data.web.pageable.default-page-size}")
    private final int defaultPageSize = 50;

    private final ProductService productService;

    private final UserUtils userUtils;


    @GetMapping(value = "findAll",produces = "application/json")
    public ResponseEntity<Object> findAll(@RequestHeader("token") String token,
                                          @RequestParam(value = "page", defaultValue = "0") Integer page,
                                          @RequestParam(value = "size", required = false) Integer size) {
        log.info("access find all products");

        UserDetailsImpl user = userUtils.getUser(token);

        String authority = user.getUser().getRole().name();
        boolean highAuth = authority.equals("ADMIN");

        boolean isInfiniteScroll = size == null;

        if (highAuth) {
            Page<ProductDto> productPage = productService.findAll(page, isInfiniteScroll ? defaultPageSize : size);
            if (productPage.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            if (!isInfiniteScroll) {
                return ResponseEntity.ok()
                        .header("x-total-count", String.valueOf(productPage.getTotalElements()))
                        .body(productPage.getContent());
            }
            return ResponseEntity.ok(productPage);
        } else {
            return ResponseEntity.status(403).body("You don't have permission to access this resource");
        }

    }


    //create product
    @PostMapping(value = "create",produces = "application/json")
    public ResponseEntity<ProductDto> add(@RequestHeader("token") String token,
                                      @RequestBody ProductDto product) {
        log.info("access add product");

        return userUtils.isAdmin(userUtils.getUser(token)) ?
                new ResponseEntity<>(productService.create(product), HttpStatus.CREATED) :
                new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    //delete product
    @DeleteMapping(value = "delete",produces = "application/json")
    public ResponseEntity<ProductDto> delete(@RequestHeader("token") String token,
                                         @RequestParam(value = "id") UUID id) {
        log.info("access delete product");
        UserDetailsImpl user = userUtils.getUser(token);

        return userUtils.isAdmin(user) ?
                new ResponseEntity<>(productService.delete(id), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }



}
