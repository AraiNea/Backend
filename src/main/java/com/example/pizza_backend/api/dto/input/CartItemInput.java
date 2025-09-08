package com.example.pizza_backend.api.dto.input;

import lombok.Data;

@Data
public class CartItemInput {
    private Long productId;
    private Integer qty;
    private Integer lineTotal;
}
