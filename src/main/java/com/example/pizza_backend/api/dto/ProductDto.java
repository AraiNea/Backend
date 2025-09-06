package com.example.pizza_backend.api.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private Long categoryId;
    private String categoryName;

    private Long productId;
    private String productName;
    private String productDetail;
    private Integer productPrice;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private Integer productStock;

    private String productImgPath;
    private Integer isActive;
    private LocalDate createdAt;
    private String createdBy;
    private LocalDate updatedAt;
    private String updatedBy;
}
