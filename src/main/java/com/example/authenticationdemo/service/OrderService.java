package com.example.authenticationdemo.service;


import com.example.authenticationdemo.exception.OrderNotFoundException;
import com.example.authenticationdemo.exception.OrderStatusConflictException;
import com.example.authenticationdemo.model.Order;
import com.example.authenticationdemo.model.dto.OrderDto;
import com.example.authenticationdemo.model.dto.ProductDto;
import com.example.authenticationdemo.model.enums.OrderStatus;
import com.example.authenticationdemo.reposity.OrderRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;

    private ModelMapper modelMapper;

    private ProductService productService;

    public Page<OrderDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable).map(Order -> modelMapper.map(Order, OrderDto.class));
    }

    /*delete*/
    public void delete(UUID id) {
        orderRepository.deleteById(id);
    }

    /*find Active orders*/
    public Page<OrderDto> findByStatus(int page, int size, String status) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByStatus(pageable,status).map(Order -> modelMapper.map(Order, OrderDto.class));
    }

    public OrderDto findById(UUID id) {
        return orderRepository.findById(id).map(order -> modelMapper.map(order, OrderDto.class)).orElseThrow(
                () -> new OrderNotFoundException(id)
        );
    }

    public Page<OrderDto> findAllByUserId(UUID id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAllByIdUserId(id, pageable).map(order -> modelMapper.map(order, OrderDto.class));
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderDto create(OrderDto dto) throws OrderStatusConflictException {
        Order order = modelMapper.map(dto, Order.class);
        order.setOrderId(UUID.randomUUID());
        if (order.getCreatedDatetime() == null) {
            order.setCreatedDatetime(String.valueOf(new Date()));
        }

        ProductDto product =  productService.findById(order.getIdProduct().getProductId());
        product.setCount(product.getCount() - order.getProductCount());
        productService.update(product);

        if(order.getStatus().equals(OrderStatus.ACTIVE)) {
            return modelMapper.map(orderRepository.save(order), OrderDto.class);
        } else {
            throw new OrderStatusConflictException(order.getOrderId(), Optional.empty(), order.getStatus());
        }
    }

    public OrderDto cancel(Order order) {
        switch (order.getStatus()) {
            case CANCELLED:
                break;
            case REJECTED:
            case FULFILLED:
                throw new OrderStatusConflictException(order.getOrderId(), Optional.of(order.getStatus()), OrderStatus.CANCELLED);
            case ACTIVE: {
                order.setStatus(OrderStatus.CANCELLED);
                order = orderRepository.save(order);
            }
        }
        return modelMapper.map(order, OrderDto.class);
    }

    public OrderDto reject(Order order) {
        switch (order.getStatus()) {
            case REJECTED:
                break;
            case CANCELLED:
            case FULFILLED:
                throw new OrderStatusConflictException(order.getOrderId(), Optional.of(order.getStatus()), OrderStatus.REJECTED);
            case ACTIVE: {
                order.setStatus(OrderStatus.REJECTED);
                order = orderRepository.save(order);
            }
        }
        return modelMapper.map(order, OrderDto.class);
    }


    public Page<OrderDto> findAllByIdUser(UUID id, Integer page, int i) {
        Pageable pageable = PageRequest.of(page, i);
        return orderRepository.findAllByIdUserId(id, pageable).map(order -> modelMapper.map(order, OrderDto.class));
    }

    public OrderDto update(OrderDto orderDto) {
        Order order = modelMapper.map(orderDto, Order.class);
        order.setOrderId(orderDto.getOrderId());
        order.setCreatedDatetime(orderDto.getCreatedDatetime());
        return modelMapper.map(orderRepository.save(order), OrderDto.class);
    }
}
