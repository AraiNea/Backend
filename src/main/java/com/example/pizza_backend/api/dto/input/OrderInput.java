package com.example.pizza_backend.api.dto.input;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderInput {
    private Long orderId;
    private Integer status;
    private Integer subtotal;
    private Integer deliveryFee;
    private Integer grandTotal;
}
