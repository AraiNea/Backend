package com.example.pizza_backend.api.dto.input;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InventoryInput {
    private LocalDate startTime;
    private LocalDate endTime;
    private String productName;
}
