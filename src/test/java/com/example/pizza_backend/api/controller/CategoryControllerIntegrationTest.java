package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.CategoryDto;
import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.input.CategoryInput;
import com.example.pizza_backend.api.dto.search.CategorySearchReq;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.service.CategoryService;
import com.example.pizza_backend.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private CategoryService categoryService;

    private CategoryDto mockCategory;
    private ProductDto mockProduct;

    @BeforeEach
    void setup() {
        mockCategory = CategoryDto.builder()
                .categoryId(1L)
                .categoryName("Pizza")
                .build();

        mockProduct = ProductDto.builder()
                .productId(101L)
                .productName("Margherita")
                .productDetail("Cheese Pizza")
                .productPrice(250)
                .build();
    }

    @Test
    void testGetAllCategories() throws Exception {
        Mockito.when(categoryService.getAllCategories()).thenReturn(List.of(mockCategory));
        Mockito.when(productService.getAllProducts(any(ProductSearchReq.class))).thenReturn(List.of(mockProduct));

        mockMvc.perform(get("/category/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories[0].categoryId").value(mockCategory.getCategoryId()))
                .andExpect(jsonPath("$.products[0].productId").value(mockProduct.getProductId()));
    }

    @Test
    void testGetAllCategoriesOnly() throws Exception {
        Mockito.when(categoryService.getAllCategories(any(CategorySearchReq.class))).thenReturn(List.of(mockCategory));

        mockMvc.perform(get("/category/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories[0].categoryId").value(mockCategory.getCategoryId()));
    }

    @Test
    void testCreateCategory() throws Exception {
        CategoryInput input = new CategoryInput();
        input.setCategoryName("New Category");

        MockMultipartFile categoryPart = new MockMultipartFile(
                "category", "", "application/json", objectMapper.writeValueAsBytes(input));
        MockMultipartFile imagePart = new MockMultipartFile(
                "image", "image.jpg", "image/jpeg", "test".getBytes());

        Mockito.when(categoryService.createCategory(any(CategoryInput.class), any(), any(String.class)))
                .thenReturn("success");

        mockMvc.perform(multipart("/category/create")
                        .file(categoryPart)
                        .file(imagePart)
                        .requestAttr("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("create success"));
    }

    @Test
    void testUpdateCategoryWithImage() throws Exception {
        CategoryInput input = new CategoryInput();
        input.setCategoryId(1L);
        input.setCategoryName("Updated Category");

        MockMultipartFile categoryPart = new MockMultipartFile(
                "category", "", "application/json", objectMapper.writeValueAsBytes(input));
        MockMultipartFile imagePart = new MockMultipartFile(
                "image", "image.jpg", "image/jpeg", "test".getBytes());

        Mockito.when(categoryService.updateCategory(any(CategoryInput.class), any(), any(String.class)))
                .thenReturn("success");

        mockMvc.perform(multipart("/category/update")
                        .file(categoryPart)
                        .file(imagePart)
                        .requestAttr("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("update success"));
    }

    @Test
    void testUpdateCategoryWithoutImage() throws Exception {
        CategoryInput input = new CategoryInput();
        input.setCategoryId(1L);
        input.setCategoryName("Updated Category");

        MockMultipartFile categoryPart = new MockMultipartFile(
                "category", "", "application/json", objectMapper.writeValueAsBytes(input));

        Mockito.when(categoryService.updateCategory(any(CategoryInput.class), any(), any(String.class)))
                .thenReturn("success");

        mockMvc.perform(multipart("/category/update")
                        .file(categoryPart)
                        .requestAttr("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("update success"));
    }

    @Test
    void testDeleteCategory() throws Exception {
        CategoryInput input = new CategoryInput();
        input.setCategoryId(1L);

        Mockito.when(categoryService.deleteCategory(any(CategoryInput.class)))
                .thenReturn("success");

        mockMvc.perform(post("/category/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("delete success"));
    }
}
