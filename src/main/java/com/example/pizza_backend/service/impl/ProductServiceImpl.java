package com.example.pizza_backend.service.impl;


import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.persistence.entity.Product;
import com.example.pizza_backend.persistence.repository.ProductRepository;
import com.example.pizza_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDto> getAllProducts(ProductSearchReq req){
        List<Product> products = productRepository.searchProducts(
                req.getProductId(),
                req.getProductName(),
                req.getProductStock(),
                req.getProductPrice(),
                req.getCategoryId(),
                req.getIsActive()
        );
        return products.stream()
                .map(p -> ProductDto.builder()
                        .productId(p.getProductId())
                        .categoryId(p.getCategory().getCategoryId())
                        .productName(p.getProductName())
                        .productDetail(p.getProductDetail())
                        .productPrice(p.getProductPrice())
                        .productStock(p.getProductStock())
                        .productImgPath(p.getProductImgPath())
                        .build())
                .toList();
    }


}
