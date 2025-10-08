package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.CategoryDto;
import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.RecommendedProductDto;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.service.CategoryService;
import com.example.pizza_backend.service.ProductService;
import com.example.pizza_backend.service.RecommendedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HomeControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private RecommendedService recommendedService;

    @InjectMocks
    private HomeController homeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    void testGetHomeInfo_success() throws Exception {
        // Mock CategoryDto
        CategoryDto cat1 = CategoryDto.builder().categoryId(1L).categoryName("Pizza").build();
        CategoryDto cat2 = CategoryDto.builder().categoryId(2L).categoryName("Drinks").build();
        List<CategoryDto> categories = List.of(cat1, cat2);

        // Mock ProductDto
        ProductDto prod1 = ProductDto.builder().productId(100L).productName("Margherita").categoryId(1L).build();
        ProductDto prod2 = ProductDto.builder().productId(101L).productName("Coke").categoryId(2L).build();
        List<ProductDto> products = List.of(prod1, prod2);

        // Mock RecommendedProductDto
        RecommendedProductDto rec1 = RecommendedProductDto.builder()
                .recommendedId(1L)
                .productId(Long.valueOf("100"))
                .recommendImgPath("img1.jpg")
                .build();
        List<RecommendedProductDto> recommended = List.of(rec1);

        // Mock service calls
        when(categoryService.getAllCategories()).thenReturn(categories);
        when(productService.getAllProducts(any(ProductSearchReq.class))).thenReturn(products);
        when(recommendedService.getAllRecommendedProducts()).thenReturn(recommended);

        mockMvc.perform(get("/home/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].category.categoryName").value("Pizza"))
                .andExpect(jsonPath("$.results[0].products[0].productName").value("Margherita"))
                .andExpect(jsonPath("$.results[1].category.categoryName").value("Drinks"))
                .andExpect(jsonPath("$.results[1].products[0].productName").value("Coke"))
                .andExpect(jsonPath("$.recommendedProducts[0].productId").value("100"))
                .andExpect(jsonPath("$.recommendedProducts[0].recommendImgPath").value("img1.jpg"));
    }

    @Test
    void testGetHomeInfo_noProducts() throws Exception {
        // Mock categories only, no products
        CategoryDto cat1 = CategoryDto.builder().categoryId(1L).categoryName("Pizza").build();
        List<CategoryDto> categories = List.of(cat1);

        when(categoryService.getAllCategories()).thenReturn(categories);
        when(productService.getAllProducts(any(ProductSearchReq.class))).thenReturn(List.of());
        when(recommendedService.getAllRecommendedProducts()).thenReturn(List.of());

        mockMvc.perform(get("/home/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].category.categoryName").value("Pizza"))
                .andExpect(jsonPath("$.results[0].products").isEmpty())
                .andExpect(jsonPath("$.recommendedProducts").isEmpty());
    }

    @Test
    void testGetHomeInfo_noCategories() throws Exception {
        // No categories, products or recommended
        when(categoryService.getAllCategories()).thenReturn(List.of());
        when(productService.getAllProducts(any(ProductSearchReq.class))).thenReturn(List.of());
        when(recommendedService.getAllRecommendedProducts()).thenReturn(List.of());

        mockMvc.perform(get("/home/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isEmpty())
                .andExpect(jsonPath("$.recommendedProducts").isEmpty());
    }
}
