package com.example.pizza_backend.service;


import com.example.pizza_backend.api.dto.CategoryDto;
import com.example.pizza_backend.persistence.repository.CategoryRepository;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();

}
