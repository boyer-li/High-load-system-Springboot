package com.example.authenticationdemo.controller;

import com.example.authenticationdemo.model.dto.WarehouseDto;
import com.example.authenticationdemo.service.WareHouseService;
import com.example.authenticationdemo.service.impl.UserDetailsImpl;
import com.example.authenticationdemo.utils.UserUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/warehouse")
@Slf4j
@RequiredArgsConstructor
public class WareHouseController {
    @Value("${spring.data.web.pageable.default-page-size}")
    private final int defaultPageSize = 50;

    private final WareHouseService wareHouseService;

    private final UserUtils userUtils;


    //find all warehouses
    @GetMapping(value = "findAll",produces = "application/json")
    public ResponseEntity<Object> findAll(@RequestHeader("token") String token,
                                          @RequestParam(value = "page", defaultValue = "0") Integer page,
                                          @RequestParam(value = "size", required = false) Integer size) {
        log.info("access find all warehouses");

        UserDetailsImpl user = userUtils.getUser(token);

        boolean isInfiniteScroll = size == null;

            Page<WarehouseDto> warehousePage = wareHouseService.findAll(page, isInfiniteScroll ? defaultPageSize : size);
            if (warehousePage.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            if (!isInfiniteScroll) {
                return ResponseEntity.ok()
                        .header("x-total-count", String.valueOf(warehousePage.getTotalElements()))
                        .body(warehousePage.getContent());
            }
            return ResponseEntity.ok(warehousePage);
    }

    //create new warehouse
    @PostMapping(value = "create",produces = "application/json")
    public ResponseEntity<Object> create(@RequestHeader("token") String token,
                                         @RequestBody WarehouseDto warehouseDto) {
        log.info("access create new warehouse");
        UserDetailsImpl user = userUtils.getUser(token);
        if(userUtils.isAdmin(user)){
            return ResponseEntity.ok(wareHouseService.create(warehouseDto));
        }else {
            return ResponseEntity.status(403).build();
        }
    }

    //delete warehouse
    @DeleteMapping(value = "delete/{id}",produces = "application/json")
    public ResponseEntity<Object> delete(@RequestHeader("token") String token,
                                         @PathVariable("id") UUID id) {
        log.info("access delete warehouse");
        UserDetailsImpl user = userUtils.getUser(token);
        if(userUtils.isAdmin(user)){
            return ResponseEntity.ok(wareHouseService.delete(id));
        }else {
            return ResponseEntity.status(403).build();
        }
    }

    //update warehouse
    @PutMapping(value = "update",produces = "application/json")
    public ResponseEntity<Object> update(@RequestHeader("token") String token,
                                         @RequestBody WarehouseDto warehouseDto) {
        log.info("access update warehouse");
        UserDetailsImpl user = userUtils.getUser(token);
        if(userUtils.isAdmin(user)){
            return ResponseEntity.ok(wareHouseService.update(warehouseDto));
        }else {
            return ResponseEntity.status(403).build();
        }
    }


}
