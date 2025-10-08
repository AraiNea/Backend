package com.example.pizza_backend.service;

import com.example.pizza_backend.api.dto.OrderDto;
import com.example.pizza_backend.api.dto.input.OrderAndItemInput;
import com.example.pizza_backend.api.dto.input.OrderInput;
import com.example.pizza_backend.api.dto.search.OrderSearchReq;

import java.util.List;

public interface OrderService {
    List<OrderDto> getOrdersByProfileId(Long profileId);
    List<OrderDto> getAllOrders(OrderSearchReq req);
    String createOrderAndOrderItems(OrderAndItemInput orderAndItemInput, Long profileId);
    String updateOrder(OrderInput orderInput, String name);

}
