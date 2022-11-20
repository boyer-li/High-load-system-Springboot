package com.example.authenticationdemo.service;


import com.example.authenticationdemo.exception.OrderNotFoundException;
import com.example.authenticationdemo.model.Product;
import com.example.authenticationdemo.model.Warehouse;
import com.example.authenticationdemo.model.dto.ProductDto;
import com.example.authenticationdemo.reposity.ProductRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    public ProductDto delete(UUID id) {
        return productRepository.findById(id).map(product -> {
            productRepository.delete(product);
            return modelMapper.map(product, ProductDto.class);
        }).orElseThrow(() -> new OrderNotFoundException(id));
    }

    public void deleteAll() {
        productRepository.deleteAll();
    }

    public Page<ProductDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable).map(product -> modelMapper.map(product, ProductDto.class));
    }

    /*create*/
    public ProductDto create(ProductDto dto) {
        Product product = modelMapper.map(dto, Product.class);
        product.setProductId(UUID.randomUUID());
        Warehouse warehouse = Warehouse.builder().warehouseId(dto.getWarehouseId()).build();
        product.setWarehouse(warehouse);
        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }

    /*update*/
    public ProductDto update(ProductDto dto) {
        Product product = modelMapper.map(dto, Product.class);
        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }

    public ProductDto findById(UUID id) {
        return productRepository.findById(id).map(product -> modelMapper.map(product, ProductDto.class)).orElseThrow(
                () -> new OrderNotFoundException(id)
        );
    }


}
