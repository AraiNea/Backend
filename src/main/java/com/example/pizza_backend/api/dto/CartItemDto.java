package com.example.pizza_backend.api.dto;

import com.example.pizza_backend.persistence.entity.Cart;
import com.example.pizza_backend.persistence.entity.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemDto {
    private Long cartItemId;
    private Long cartId;
    private Long productId;
    private String productName;
    private Integer productPrice;
    private Integer qty;
    private Integer lineTotal;
}
