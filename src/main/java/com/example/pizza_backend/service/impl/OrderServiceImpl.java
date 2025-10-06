package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.OrderDto;
import com.example.pizza_backend.persistence.entity.Orders;
import com.example.pizza_backend.persistence.repository.OrderRepository;
import com.example.pizza_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public List<OrderDto> getOrdersByProfileId(Long profileId) {
        List<Orders> orders = orderRepository.getOrdersByProfileProfileId(profileId);
        return orders.stream()
                .map(order -> OrderDto.builder()
                        .orderId(order.getOrderId())
                        .username(order.getProfile().getUsername())
                        .status(order.getStatus())
                        .subtotal(order.getSubtotal())
                        .deliveryFee(order.getDeliveryFee())
                        .grandTotal(order.getGrandTotal())
                        .createdAt(order.getCreatedAt())
                        .build())
                .toList();
    }
}
