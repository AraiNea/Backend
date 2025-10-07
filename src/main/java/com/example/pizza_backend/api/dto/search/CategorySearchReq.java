package com.example.pizza_backend.api.dto.search;

import lombok.Data;

@Data
public class CategorySearchReq {
    private String categoryName;
    private Long categoryId;
}
