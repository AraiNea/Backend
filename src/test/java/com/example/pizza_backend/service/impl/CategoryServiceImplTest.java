package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.FileUploadUtil;
import com.example.pizza_backend.api.dto.CategoryDto;
import com.example.pizza_backend.api.dto.input.CategoryInput;
import com.example.pizza_backend.api.dto.search.CategorySearchReq;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.exception.IdNotFoundException;
import com.example.pizza_backend.persistence.entity.Category;
import com.example.pizza_backend.persistence.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private Mapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ===== getAllCategories =====
    @Test
    void getAllCategories_shouldReturnCategoryDtoList_whenReqIsNotNull() {
        CategorySearchReq req = new CategorySearchReq();
        req.setCategoryId(1L);
        req.setCategoryName("Pizza");

        Category category = new Category();
        CategoryDto categoryDto = mock(CategoryDto.class);

        when(categoryRepository.searchCategory(1L, "Pizza")).thenReturn(List.of(category));
        when(mapper.toCategoryDto(category)).thenReturn(categoryDto);

        List<CategoryDto> result = categoryService.getAllCategories(req);
        assertThat(result).hasSize(1).contains(categoryDto);

        verify(categoryRepository).searchCategory(1L, "Pizza");
        verify(mapper).toCategoryDto(category);
    }

    @Test
    void getAllCategories_shouldReturnAll_whenReqIsNull() {
        Category category = new Category();
        CategoryDto categoryDto = mock(CategoryDto.class);

        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(mapper.toCategoryDto(category)).thenReturn(categoryDto);

        List<CategoryDto> result = categoryService.getAllCategories(null);
        assertThat(result).hasSize(1).contains(categoryDto);

        verify(categoryRepository).findAll();
        verify(mapper).toCategoryDto(category);
    }

    // ===== createCategory =====
    @Test
    void createCategory_shouldReturnSuccess() throws IOException {
        CategoryInput input = new CategoryInput();
        input.setCategoryName("Pizza");

        MockMultipartFile file = new MockMultipartFile(
                "image", "pizza.png", "image/png", "dummy content".getBytes()
        );

        Category category = new Category();
        when(mapper.toCategory(input, "admin")).thenReturn(category);

        // spy FileUploadUtil
        try (MockedStatic<FileUploadUtil> utilities = mockStatic(FileUploadUtil.class)) {
            String result = categoryService.createCategory(input, file, "admin");
            assertThat(result).isEqualTo("success");

            verify(categoryRepository).save(category);
            utilities.verify(() -> FileUploadUtil.saveFile("Images/category-photos/", file, StringUtils.cleanPath(file.getOriginalFilename())));
        }
    }

    // ===== updateCategory =====
    @Test
    void updateCategory_shouldReturnSuccess_whenImagePresent() throws IOException {
        CategoryInput input = new CategoryInput();
        input.setCategoryId(1L);

        MockMultipartFile file = new MockMultipartFile(
                "image", "updated.png", "image/png", "content".getBytes()
        );

        Category category = new Category();
        category.setCategoryImg("old.png");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(mapper).updateCategoryFromInput(input, category, "admin");

        try (MockedStatic<FileUploadUtil> utilities = mockStatic(FileUploadUtil.class)) {
            String result = categoryService.updateCategory(input, file, "admin");
            assertThat(result).isEqualTo("success");

            verify(categoryRepository).save(category);
            utilities.verify(() -> FileUploadUtil.deleteFile("Images/category-photos/", "old.png"));
            utilities.verify(() -> FileUploadUtil.saveFile("Images/category-photos/", file, StringUtils.cleanPath(file.getOriginalFilename())));
        }
    }

    @Test
    void updateCategory_shouldThrowException_whenCategoryIdNull() {
        CategoryInput input = new CategoryInput();
        input.setCategoryId(null);
        assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(input, null, "admin"));
    }

    @Test
    void updateCategory_shouldThrowException_whenCategoryNotFound() {
        CategoryInput input = new CategoryInput();
        input.setCategoryId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> categoryService.updateCategory(input, null, "admin"));
    }

    // ===== deleteCategory =====
    @Test
    void deleteCategory_shouldReturnSuccess() throws IOException {
        CategoryInput input = new CategoryInput();
        input.setCategoryId(1L);

        Category category = new Category();
        category.setCategoryImg("old.png");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        try (MockedStatic<FileUploadUtil> utilities = mockStatic(FileUploadUtil.class)) {
            String result = categoryService.deleteCategory(input);
            assertThat(result).isEqualTo("success");

            verify(categoryRepository).deleteById(1L);
            utilities.verify(() -> FileUploadUtil.deleteFile("Images/category-photos/", "old.png"));
        }
    }

    @Test
    void deleteCategory_shouldThrowException_whenCategoryIdNull() {
        CategoryInput input = new CategoryInput();
        input.setCategoryId(null);
        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(input));
    }

    @Test
    void deleteCategory_shouldThrowException_whenCategoryNotFound() {
        CategoryInput input = new CategoryInput();
        input.setCategoryId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> categoryService.deleteCategory(input));
    }
}
