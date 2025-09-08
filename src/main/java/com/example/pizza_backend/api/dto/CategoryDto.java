package com.example.pizza_backend.api.dto;



import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto {
    private Long categoryId;
    private String categoryName;
    private String categoryImg;
    private String categoryProductPath;
    private Long categoryPriority;
}
