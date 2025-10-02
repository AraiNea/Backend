package com.example.pizza_backend.api.dto.input;

import lombok.Data;

@Data
public class CategoryInput {
    private Long categoryId;
    private String categoryName;
    private String categoryPriority;
    private Integer categoryProductPath;
}
