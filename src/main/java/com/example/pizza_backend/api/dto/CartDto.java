package com.example.pizza_backend.api.dto;

import com.example.pizza_backend.persistence.entity.CartItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartDto {
    private Long cartId;
    private Long profileId;
    private String username;
    private LocalDate createdAt;
    private String note;
    private List<CartItem> cartItems;
}
