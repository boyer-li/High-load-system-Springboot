package com.example.authenticationdemo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDto implements Serializable {
    private  UUID receiptId;
    private  OrderDto order;
    private  String description;
    private  String title;
}