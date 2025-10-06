package com.example.pizza_backend.api.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendedProductDto {
    private Long recommendedId;
    private String productId;
    private String recommendedImg;
    private Integer priority;
}
