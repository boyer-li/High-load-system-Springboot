package com.example.authenticationdemo.model.dto;

import com.example.authenticationdemo.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto implements Serializable {
    private  UUID orderId;
    private OrderStatus status;
    private  UUID idProduct;
    private  UUID idUser;
    private  String createdDatetime;
    private  int productCount;
}