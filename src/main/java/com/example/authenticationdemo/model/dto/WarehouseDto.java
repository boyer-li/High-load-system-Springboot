package com.example.authenticationdemo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDto implements Serializable {
    private  UUID warehouseId;
    private  String address;
    private  boolean closed;
    private  String title;
    private  String name;
}