package com.example.pizza_backend.service;

import com.example.pizza_backend.api.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> getOrdersByProfileId(Long profileId);
}
