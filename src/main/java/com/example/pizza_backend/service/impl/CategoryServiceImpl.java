package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.CategoryDto;
import com.example.pizza_backend.persistence.entity.Category;
import com.example.pizza_backend.persistence.repository.CategoryRepository;
import com.example.pizza_backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category>  categories = categoryRepository.findAllCategory();
        return categories.stream()
                .map((c-> CategoryDto.builder()
                        .categoryId(c.getCategoryId())
                        .categoryName(c.getCategoryName())
                        .build()))
                .toList();
    }
}
