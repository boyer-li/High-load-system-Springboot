package com.example.authenticationdemo.exception;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(UUID id) {
        super("Could not find order " + id);
    }
}