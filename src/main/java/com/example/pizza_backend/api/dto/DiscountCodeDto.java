package com.example.pizza_backend.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiscountCodeDto {
    private Long discountId;
    private String code;
    private Integer value;
    private Long productId;
}
