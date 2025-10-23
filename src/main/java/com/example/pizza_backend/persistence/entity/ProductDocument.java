package com.example.pizza_backend.persistence.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@Document(indexName = "product")
@Data
@Builder
public class ProductDocument {
    @Id
    private Long productId;
    private Long categoryId;
    private String productName;
    private String productDetail;
    private String productImg;
    private Integer productPrice;
    private Integer productStock;
    private Integer isActive;
    private String productImgPath;
}