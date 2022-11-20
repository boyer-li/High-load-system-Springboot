package com.example.authenticationdemo.reposity;

import com.example.authenticationdemo.model.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {
    Page<Receipt> findAllByIdUserId(UUID userId, Pageable pageable);
}