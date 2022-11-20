package com.example.authenticationdemo.service;


import com.example.authenticationdemo.model.Receipt;
import com.example.authenticationdemo.model.dto.ReceiptDto;
import com.example.authenticationdemo.reposity.ReceiptRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReceiptService {
    private ReceiptRepository receiptRepository;
    private ModelMapper modelMapper;

    /*findAll*/
    public Page<ReceiptDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return receiptRepository.findAll(pageable).map(Receipt -> modelMapper.map(Receipt, ReceiptDto.class));
    }

    /*findAllByUserId*/
    public Page<ReceiptDto> findAllByUserId(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return receiptRepository.findAllByIdUserId(userId, pageable).map(Receipt -> modelMapper.map(Receipt, ReceiptDto.class));
    }

    /*create*/
    public ReceiptDto create(ReceiptDto dto) {
        Receipt receipt = modelMapper.map(dto, Receipt.class);
        receipt.setReceiptId(UUID.randomUUID());
        if (receipt.getCreatedDatetime() == null) {
            receipt.setCreatedDatetime(Instant.now());
        }
        return modelMapper.map(receiptRepository.save(receipt), ReceiptDto.class);
    }

    /*delete*/
    public void delete(UUID id) {
        receiptRepository.deleteById(id);
    }






}
