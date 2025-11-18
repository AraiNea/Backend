package com.example.pizza_backend.api.dto.input;

import com.example.pizza_backend.api.dto.DiscountCodeDto;
import lombok.Data;

import java.util.List;

@Data
public class OrderAndItemInput {
    private OrderInput orderInput;
    private List<CartItemInput> cartItemInputs;
    private DiscountCodeDto discountCode;
}
