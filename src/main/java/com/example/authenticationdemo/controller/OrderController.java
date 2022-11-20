package com.example.authenticationdemo.controller;

import com.example.authenticationdemo.model.dto.OrderDto;
import com.example.authenticationdemo.model.dto.ProductDto;
import com.example.authenticationdemo.model.enums.OrderStatus;
import com.example.authenticationdemo.service.OrderService;
import com.example.authenticationdemo.service.ProductService;
import com.example.authenticationdemo.service.impl.UserDetailsImpl;
import com.example.authenticationdemo.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    @Value("${spring.data.web.pageable.default-page-size}")
    private final int defaultPageSize = 50;

    private final OrderService orderService;

    private final UserUtils userUtils;


    @GetMapping(value = "findAll",produces = "application/json")
    public ResponseEntity<Object> findAll(@RequestHeader("token") String token,
                                               @RequestParam(value = "page", defaultValue = "0") Integer page,
                                               @RequestParam(value = "size", required = false) Integer size) {
        log.info("access find all orders");
        UserDetailsImpl user = userUtils.getUser(token);

        String authority = user.getUser().getRole().name();
        boolean highAuth = authority.equals("ADMIN");

        boolean isInfiniteScroll = size == null;

        //如果是管理员，可以查看所有订单，否则只能查看自己的订单（admin can check all orders, but user just can check the order belongs to self）
        Page<OrderDto> orderPage = highAuth ?
                orderService.findAll(page, isInfiniteScroll ? defaultPageSize : size) :
                orderService.findAllByIdUser(user.getUser().getId(), page, isInfiniteScroll ? defaultPageSize : size);

        if (orderPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        if (!isInfiniteScroll) {
            return ResponseEntity.ok()
                    .header("x-total-count", String.valueOf(orderPage.getTotalElements()))
                    .body(orderPage.getContent());
        }

        return ResponseEntity.ok().body(orderPage);
    }

    @GetMapping(value = "/findById/{id}")
    public ResponseEntity<OrderDto> findById(@RequestHeader("token") String header,
                                             @PathVariable UUID id) {
        OrderDto orderDto = orderService.findById(id);
        return userUtils.isAllowed(userUtils.getUser(header), orderDto.getOrderId()) ?
                ResponseEntity.ok(orderDto) :
                new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping(value = "/create" , consumes = "application/json")
    public ResponseEntity<OrderDto> createOrder(@RequestHeader("token") String header,
                                                @RequestBody OrderDto order) {
        if (order.getIdUser() == null) {
            order.setIdUser(userUtils.getUser(header).getUser().getId());
        }

        return userUtils.isAllowed(userUtils.getUser(header), order.getIdUser()) ?
                new ResponseEntity<>(orderService.create(order), HttpStatus.CREATED) :
                new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    //cancel order  取消订单
    @PutMapping(value = "/cancel/{id}")
    public ResponseEntity<OrderDto> cancelOrder(@RequestHeader("token") String header,
                                                @PathVariable String id) {
        UUID orderId = UUID.fromString(id);

        OrderDto orderDto = orderService.findById(orderId);
        if (orderDto == null) {
            return ResponseEntity.notFound().build();
        }
        if (!userUtils.isAllowed(userUtils.getUser(header), orderDto.getOrderId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        orderDto.setStatus(OrderStatus.CANCELLED);
        return ResponseEntity.ok(orderService.update(orderDto));
    }



}
