package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.OrderItemDto;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.persistence.entity.OrderItem;
import com.example.pizza_backend.persistence.repository.OrderItemRepository;
import com.example.pizza_backend.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final Mapper mapper;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, Mapper mapper) {
        this.orderItemRepository = orderItemRepository;
        this.mapper = mapper;
    }


    @Override
    public List<OrderItemDto> getOrderItems(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderOrderId(orderId);
        return orderItems.stream()
                .map(item -> mapper.toOrderItemDto(item))
                .toList();
    }
}
