package com.example.pizza_backend.service;

import com.example.pizza_backend.api.dto.RecommendedProductDto;
import com.example.pizza_backend.api.dto.input.ProductInput;
import com.example.pizza_backend.api.dto.input.RecommendedInput;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RecommendedService {
    List<RecommendedProductDto> getAllRecommendedProducts();
    String createRecommended(RecommendedInput recommendedInput) throws IOException;
    String deleteRecommended(RecommendedInput recommendedInput) throws IOException;
}
