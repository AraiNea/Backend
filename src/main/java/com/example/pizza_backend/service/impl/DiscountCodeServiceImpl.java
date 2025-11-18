package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.DiscountCodeDto;
import com.example.pizza_backend.api.dto.search.DiscountCodeSearchReq;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.exception.IdNotFoundException;
import com.example.pizza_backend.persistence.entity.Cart;
import com.example.pizza_backend.persistence.entity.DiscountCode;
import com.example.pizza_backend.persistence.repository.DiscountCodeRepository;
import com.example.pizza_backend.service.DiscountCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountCodeServiceImpl implements DiscountCodeService {

    private DiscountCodeRepository discountCodeRepository;
    private Mapper mapper;

    @Autowired
    public DiscountCodeServiceImpl(DiscountCodeRepository discountCodeRepository, Mapper mapper) {
        this.discountCodeRepository = discountCodeRepository;
        this.mapper = mapper;
    }

    @Override
    public DiscountCodeDto getDiscountCode(DiscountCodeSearchReq req) {
        if (req.getCode() == null || req.getCode() == null){
            throw new IllegalArgumentException("The given code cannot be null");
        }
        DiscountCode code = discountCodeRepository.findByCode(req.getCode())
                .orElseThrow(() -> new IdNotFoundException("Code not found"));
        return mapper.toDiscountCodeDto(code);
    }
}
