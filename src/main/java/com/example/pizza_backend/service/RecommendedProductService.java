package com.example.pizza_backend.service;

import com.example.pizza_backend.api.dto.RecommendedProductDto;

import java.util.List;

public interface RecommendedProductService {
    List<RecommendedProductDto> getAllRecommendedProducts();
}
