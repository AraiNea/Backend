package com.example.pizza_backend.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDto {
    private Long orderItemId;
    private Long productIdSnapshot;
    private String productName;
    private String productDetail;
    private Integer productPrice;
    private Integer qty;
    private Integer lineTotal;
}
