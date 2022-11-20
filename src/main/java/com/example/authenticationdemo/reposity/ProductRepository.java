package com.example.authenticationdemo.reposity;

import com.example.authenticationdemo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}