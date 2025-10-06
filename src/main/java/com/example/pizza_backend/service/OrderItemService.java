package com.example.pizza_backend.service;

import com.example.pizza_backend.api.dto.OrderItemDto;

import java.util.List;

public interface OrderItemService {
    List<OrderItemDto> getOrderItems(Long orderId);
}
