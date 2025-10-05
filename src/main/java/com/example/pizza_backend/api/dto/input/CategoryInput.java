package com.example.pizza_backend.api.dto.input;

import lombok.Data;

@Data
public class CategoryInput {
    private Long categoryId;
    private String categoryName;
    private Integer categoryPriority;
    private String categoryProductPath;
}
