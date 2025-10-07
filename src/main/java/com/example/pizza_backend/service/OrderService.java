package com.example.pizza_backend.service;

import com.example.pizza_backend.api.dto.OrderDto;
import com.example.pizza_backend.api.dto.input.OrderAndItemInput;
import com.example.pizza_backend.api.dto.input.OrderInput;

import java.util.List;

public interface OrderService {
    List<OrderDto> getOrdersByProfileId(Long profileId);
    String createOrderAndOrderItems(OrderAndItemInput orderAndItemInput, Long profileId);
    String updateOrder(OrderInput orderInput, String name);
}
