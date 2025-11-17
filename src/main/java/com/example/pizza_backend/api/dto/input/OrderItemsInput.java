package com.example.pizza_backend.api.dto.input;

import com.example.pizza_backend.api.dto.OrderItemDto;
import lombok.Data;

import java.util.List;

@Data
public class OrderItemsInput {
    private List<OrderItemDto> orderItems;
}
