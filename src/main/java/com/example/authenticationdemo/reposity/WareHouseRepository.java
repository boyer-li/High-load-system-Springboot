package com.example.authenticationdemo.reposity;

import com.example.authenticationdemo.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WareHouseRepository extends JpaRepository<Warehouse, UUID> {

//    find opening warehouse
    List<Warehouse> findAllByClosedIsFalse();
}