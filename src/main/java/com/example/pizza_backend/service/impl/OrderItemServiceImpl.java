package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.OrderItemDto;
import com.example.pizza_backend.persistence.entity.OrderItem;
import com.example.pizza_backend.persistence.repository.OrderItemRepository;
import com.example.pizza_backend.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }


    @Override
    public List<OrderItemDto> getOrderItems(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderOrderId(orderId);
        return orderItems.stream()
                .map(item -> OrderItemDto.builder()
                        .orderItemId(item.getOrderItemId())
                        .productIdSnapshot(item.getProductIdSnapshot())
                        .productName(item.getProductName())
                        .productDetail(item.getProductDetail())
                        .productPrice(item.getProductPrice())
                        .qty(item.getQty())
                        .lineTotal(item.getLineTotal())
                        .build()
                )
                .toList();
    }
}
