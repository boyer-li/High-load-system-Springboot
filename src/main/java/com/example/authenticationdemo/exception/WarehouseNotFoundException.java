package com.example.authenticationdemo.exception;

import java.util.UUID;

public class WarehouseNotFoundException extends RuntimeException {
    public WarehouseNotFoundException(UUID id) {
        super("Could not find WareHouse " + id);
    }
}

