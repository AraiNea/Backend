package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.RecommendedProductDto;
import com.example.pizza_backend.persistence.entity.RecommendedProduct;
import com.example.pizza_backend.persistence.repository.RecommendedProductRepository;
import com.example.pizza_backend.service.RecommendedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendedProductImpl implements RecommendedProductService {

    private final RecommendedProductRepository recommendedProductRepository;

    @Autowired
    public RecommendedProductImpl(RecommendedProductRepository recommendedProductRepository) {
        this.recommendedProductRepository = recommendedProductRepository;
    }
    @Override
    public List<RecommendedProductDto> getAllRecommendedProducts() {
        List<RecommendedProduct> recommendedProducts = recommendedProductRepository.findAll();
        return recommendedProducts.stream()
                .map(r -> RecommendedProductDto.builder()
                        .recommendedId(r.getRecommendedId())
                        .recommendedImg(r.getRecommendImgPath())
                        .build())
                .toList();
    }
}
