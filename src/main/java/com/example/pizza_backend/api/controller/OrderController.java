package com.example.pizza_backend.api.controller;


import com.example.pizza_backend.api.dto.*;
import com.example.pizza_backend.persistence.repository.OrderRepository;
import com.example.pizza_backend.service.OrderItemService;
import com.example.pizza_backend.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @Autowired
    public OrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getOrders(HttpServletRequest request) {
        // ดึง profile_id ที่ถูก set มาจาก Interceptor
        Long profileId = (Long) request.getAttribute("profile_id");

        List<OrderDto> orders = orderService.getOrdersByProfileId(profileId);

        List<Map<String, Object>> results = new ArrayList<>();
        for (OrderDto order : orders) {
            Map<String, Object> data = Map.of(
                    "order", order,
                    "orderItems", orderItemService.getOrderItems(order.getOrderId())
            );
            results.add(data);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("results", results);

        return ResponseEntity.ok(response);
    }
}
