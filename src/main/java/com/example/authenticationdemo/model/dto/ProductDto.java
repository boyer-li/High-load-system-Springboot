package com.example.authenticationdemo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable {
    private  UUID productId;
    private  UUID warehouseId;
    private  String name;
    private  int price;
    private  String description;
    private  String category;
    private  int count;
}