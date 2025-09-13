package com.example.pizza_backend.api.dto.input;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProductInput {
    private String productName;
    private String productDetail;
    private Integer productPrice;
    private Integer productStock;
    private String productImgPath; //ex. cola.png
    private Integer isActive;
}
