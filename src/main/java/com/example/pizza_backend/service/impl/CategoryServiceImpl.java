package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.FileUploadUtil;
import com.example.pizza_backend.api.dto.CategoryDto;
import com.example.pizza_backend.api.dto.input.CategoryInput;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.exception.IdNotFoundException;
import com.example.pizza_backend.persistence.entity.Category;
import com.example.pizza_backend.persistence.repository.CategoryRepository;
import com.example.pizza_backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final Mapper mapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, Mapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category>  categories = categoryRepository.findAllByOrderByCategoryPriorityAsc();
        return categories.stream()
                .map((c-> CategoryDto.builder()
                        .categoryId(c.getCategoryId())
                        .categoryName(c.getCategoryName())
                        .categoryImg(c.getCategoryImg())
                        .categoryProductPath(c.getCategoryProductPath())
                        .categoryPriority(c.getCategoryPriority())
                        .build()))
                .toList();
    }

    @Override
    @Transactional
    public String createCategory(CategoryInput categoryInput, MultipartFile imageFile, String username) throws IOException {
        Category category = mapper.toCategory(categoryInput, username);
        String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
        category.setCategoryImg(fileName);
        categoryRepository.save(category);

        FileUploadUtil.saveFile("Images/category-photos/",imageFile,fileName);

        return "success";
    }

    @Override
    @Transactional
    public String updateCategory(CategoryInput categoryInput, MultipartFile imageFile, String username) throws IOException {
        if (categoryInput.getCategoryId() == null) {
            throw new IllegalArgumentException("The given category Id cannot be null");
        }
        Long categoryId = categoryInput.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IdNotFoundException("Category Not found"));
        mapper.updateCategoryFromInput(categoryInput, category, username);

        if (imageFile != null && !imageFile.isEmpty()){
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            FileUploadUtil.deleteFile("Images/category-photos/",category.getCategoryImg());
            category.setCategoryImg(fileName);
            FileUploadUtil.saveFile("Images/category-photos/",imageFile,fileName);
        }

        categoryRepository.save(category);
        return "success";
    }

    @Override
    @Transactional
    public String deleteCategory(CategoryInput categoryInput) throws IOException {
        if (categoryInput.getCategoryId() == null) {
            throw new IllegalArgumentException("The given category Id cannot be null");
        }
        Long categoryId = categoryInput.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IdNotFoundException("Category Not found"));
        String filename = category.getCategoryImg();

        categoryRepository.deleteById(categoryId);
        FileUploadUtil.deleteFile("Images/category-photos/",filename);
        return "success";
    }
}
