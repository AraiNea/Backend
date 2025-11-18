package com.example.pizza_backend.service;

import com.example.pizza_backend.api.dto.DiscountCodeDto;
import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.search.DiscountCodeSearchReq;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;

import java.util.List;

public interface DiscountCodeService {
    DiscountCodeDto getDiscountCode(DiscountCodeSearchReq req);
}
