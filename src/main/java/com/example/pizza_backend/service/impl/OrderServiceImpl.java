package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.OrderDto;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.persistence.entity.Orders;
import com.example.pizza_backend.persistence.repository.OrderRepository;
import com.example.pizza_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private Mapper mapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, Mapper mapper) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }
    public List<OrderDto> getOrdersByProfileId(Long profileId) {
        List<Orders> orders = orderRepository.getOrdersByProfileProfileId(profileId);
        return orders.stream()
                .map(order -> mapper.toOrderDto(order))
                .toList();
    }
}
