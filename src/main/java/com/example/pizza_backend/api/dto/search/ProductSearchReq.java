package com.example.pizza_backend.api.dto.search;

import lombok.Data;

@Data
public class ProductSearchReq {
    private Long  productId;
    private String productName;
    private Integer productPrice;
    private Integer productStock;
    private Long categoryId;
    private Integer isActive;
}
