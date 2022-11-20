package com.example.authenticationdemo.reposity;

import com.example.authenticationdemo.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

//    find Active Orders

    Page<Order> findByStatus(Pageable pageable, String status);

    Page<Order> findAllByIdUserId(UUID id, Pageable pageable);
}