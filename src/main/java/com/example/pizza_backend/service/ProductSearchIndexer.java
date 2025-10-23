package com.example.pizza_backend.service;

import com.example.pizza_backend.persistence.entity.Product;
import com.example.pizza_backend.persistence.entity.ProductDocument;
import com.example.pizza_backend.persistence.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSearchIndexer {
    @Autowired private ProductRepository productRepository;
    @Autowired private ElasticsearchOperations operations;

    @PostConstruct
    public void reindex() {
        List<Product> products = productRepository.findAll();

        List<ProductDocument> docs = products.stream()
                .map(p -> ProductDocument.builder()
                        .productId(p.getProductId())
                        .categoryId(p.getCategory().getCategoryId())
                        .productName(p.getProductName())
                        .productDetail(p.getProductDetail())
                        .productImg(p.getProductImg())
                        .productPrice(p.getProductPrice())
                        .productStock(p.getProductStock())
                        .isActive(p.getIsActive())
                        .productImgPath(p.getProductImgPath())
                        .build()
                ).toList();


        docs.forEach(operations::save);
        System.out.println("✅ Indexed " + docs.size() + " products");
    }
}

