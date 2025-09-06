package com.example.pizza_backend.service;

import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;

import java.util.List;

public interface ProductService {

    List<ProductDto> getAllProducts(ProductSearchReq productSearchReq);
}
