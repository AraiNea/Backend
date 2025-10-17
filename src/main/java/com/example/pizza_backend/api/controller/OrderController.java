package com.example.pizza_backend.api.controller;


import com.example.pizza_backend.api.dto.*;
import com.example.pizza_backend.api.dto.input.OrderAndItemInput;
import com.example.pizza_backend.api.dto.input.OrderInput;
import com.example.pizza_backend.api.dto.search.OrderSearchReq;
import com.example.pizza_backend.service.CartItemService;
import com.example.pizza_backend.service.OrderItemService;
import com.example.pizza_backend.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final CartItemService cartItemService;

    @Autowired
    public OrderController(OrderService orderService, OrderItemService orderItemService, CartItemService cartItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.cartItemService = cartItemService;
    }

    @GetMapping("/")
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

    @GetMapping("/list")
    public ResponseEntity<?> getAllOrders(OrderSearchReq req) {

        List<OrderDto> orders = orderService.getAllOrders(req);

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

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(HttpServletRequest request,
                                         @RequestBody OrderAndItemInput orderAndItemInput) {
        Long profileId = (Long) request.getAttribute("profile_id");
        Integer profileRole = (Integer) request.getAttribute("profile_role");
        if (profileRole==2) {
            return  ResponseEntity.badRequest()
                    .body(Map.of("message", "you are admin"));
        }
        String createLog = orderService.createOrderAndOrderItems(orderAndItemInput, profileId);
        String deleteLog = cartItemService.clearAllCartItem(profileId);
        if (createLog == "success" && deleteLog == "success") {
            return  ResponseEntity.ok()
                    .body(Map.of("message", "create order and clear cart success"));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateOrder(HttpServletRequest request,
                                         @RequestBody OrderInput orderInput) {
        String username = (String) request.getAttribute("username");
        String createLog = orderService.updateOrder(orderInput, username);
        if (createLog == "success") {
            return  ResponseEntity.ok()
                    .body(Map.of("message", "Update order success"));
        }
        return ResponseEntity.badRequest().build();
    }
}
