package com.example.pizza_backend.api.dto.search;

import lombok.Data;

@Data
public class ProductSearchReq {
    private Long  productId;
    private String productName;
    private String productPrice;
    private String productStock;
    private Long categoryId;
    private String isActive;
}
