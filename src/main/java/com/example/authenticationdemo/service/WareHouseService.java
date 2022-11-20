package com.example.authenticationdemo.service;

import com.example.authenticationdemo.exception.WarehouseNotFoundException;
import com.example.authenticationdemo.model.Warehouse;
import com.example.authenticationdemo.model.dto.WarehouseDto;
import com.example.authenticationdemo.reposity.WareHouseRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class WareHouseService {
    private WareHouseRepository wareHouseRepository;
    private ModelMapper modelMapper;

    /*create*/
    public WarehouseDto create(WarehouseDto dto) {
        Warehouse warehouse = modelMapper.map(dto, Warehouse.class);
        return modelMapper.map(wareHouseRepository.save(warehouse), WarehouseDto.class);
    }

    /*update*/
    public WarehouseDto update(WarehouseDto dto) {
        Warehouse warehouse = modelMapper.map(dto, Warehouse.class);
        return modelMapper.map(wareHouseRepository.save(warehouse), WarehouseDto.class);
    }

    public WarehouseDto delete(UUID id) {
        return wareHouseRepository.findById(id).map(warehouse -> {
            wareHouseRepository.delete(warehouse);
            return modelMapper.map(warehouse, WarehouseDto.class);
        }).orElseThrow(() -> new WarehouseNotFoundException(id));
    }

    /*findAll*/
    public Page<WarehouseDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return wareHouseRepository.findAll(pageable).map(warehouse -> modelMapper.map(warehouse, WarehouseDto.class));
    }

    /*findById*/
    public WarehouseDto findById(UUID id) {
        return wareHouseRepository.findById(id).map(warehouse -> modelMapper.map(warehouse, WarehouseDto.class)).orElseThrow(
                () -> new WarehouseNotFoundException(id)
        );
    }

}
