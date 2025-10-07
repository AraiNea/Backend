package com.example.pizza_backend.api.dto.input;

import lombok.Data;

@Data
public class CartItemInput {
    private Long cartItemId;
    private Long productId;
    private Integer qty;
    private Integer lineTotal;

    //สำหรับเพิ่มใส่ order
    private String productName;
    private String productDetail;
    private Double productPrice;
}
