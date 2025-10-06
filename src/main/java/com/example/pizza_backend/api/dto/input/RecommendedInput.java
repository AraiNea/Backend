package com.example.pizza_backend.api.dto.input;

import lombok.Data;

@Data
public class RecommendedInput {
    private Long recommendedId;
    private Long productId;
}
