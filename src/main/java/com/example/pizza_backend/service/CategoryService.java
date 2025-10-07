package com.example.pizza_backend.service;


import com.example.pizza_backend.api.dto.CategoryDto;
import com.example.pizza_backend.api.dto.input.CategoryInput;
import com.example.pizza_backend.api.dto.input.ProductInput;
import com.example.pizza_backend.api.dto.search.CategorySearchReq;
import com.example.pizza_backend.persistence.repository.CategoryRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories(CategorySearchReq req);
    default List<CategoryDto> getAllCategories() {
        return getAllCategories(null);
    }
    String createCategory(CategoryInput categoryInput, MultipartFile imageFile, String username) throws IOException;
    String updateCategory(CategoryInput categoryInput, MultipartFile imageFile, String username) throws IOException;
    String deleteCategory(CategoryInput categoryInput) throws IOException;
}
