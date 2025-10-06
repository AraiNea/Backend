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
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HomeControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private RecommendedService recommendedService;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getHomeInfo_shouldReturnCategoriesProductsAndRecommendations_withAllFields() {
        ProductSearchReq req = new ProductSearchReq();

        // 🔹 Mock CategoryDto
        CategoryDto category1 = mock(CategoryDto.class);
        when(category1.getCategoryName()).thenReturn("Pizza");
        when(category1.getCategoryId()).thenReturn(1L);

        // 🔹 Mock ProductDto
        ProductDto product1 = mock(ProductDto.class);
        when(product1.getProductName()).thenReturn("Hawaiian");
        when(product1.getCategoryId()).thenReturn(1L);

        // 🔹 Mock RecommendedProductDto พร้อม field ทุกตัว
        RecommendedProductDto rec1 = mock(RecommendedProductDto.class);
        when(rec1.getRecommendedId()).thenReturn(500L);
        when(rec1.getProductId()).thenReturn("P101");
        when(rec1.getRecommendedImg()).thenReturn("pepperoni.png");
        //JOMJAI DELETE THIS LINE BELOW
        // when(rec1.getRecProductPath()).thenReturn("/recommended/pepperoni");
//        when(rec1.getPriority()).thenReturn(1);

        // 🔹 Mock service responses
        when(categoryService.getAllCategories()).thenReturn(List.of(category1));
        when(productService.getAllProducts(req)).thenReturn(List.of(product1));
        when(recommendedService.getAllRecommendedProducts()).thenReturn(List.of(rec1));

        // Act
        ResponseEntity<Map<String, Object>> response = homeController.getHomeInfo(req);

        // Assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body).containsKeys("results", "recommendedProducts");

        // 🔹 ตรวจสอบ category
        List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");
        List<CategoryDto> categoriesFromResult = results.stream()
                .map(r -> (CategoryDto) r.get("category"))
                .toList();
        assertThat(categoriesFromResult)
                .extracting(CategoryDto::getCategoryName)
                .contains("Pizza");

        // 🔹 ตรวจสอบ product
        List<ProductDto> products = (List<ProductDto>) results.get(0).get("products");
        assertThat(products)
                .extracting(ProductDto::getProductName)
                .contains("Hawaiian");

        // 🔹 ตรวจสอบ recommendedProducts พร้อมทุก field
        List<RecommendedProductDto> recommends = (List<RecommendedProductDto>) body.get("recommendedProducts");
        assertThat(recommends)
                .extracting(RecommendedProductDto::getRecommendedId)
                .contains(500L);

        assertThat(recommends)
                .extracting(RecommendedProductDto::getProductId)
                .contains("P101");

        assertThat(recommends)
                .extracting(RecommendedProductDto::getRecommendedImg)
                .contains("pepperoni.png");

        //JOMJAI DELETE THIS LINE BELOW
//        assertThat(recommends)
//                .extracting(RecommendedProductDto::getRecProductPath)
//                .contains("/recommended/pepperoni");
//        assertThat(recommends)
//                .extracting(RecommendedProductDto::getPriority)
//                .contains(1);
    }

    @Test
    void getHomeInfo_shouldHandleEmptyLists() {
        ProductSearchReq req = new ProductSearchReq();

        // Mock services คืน empty list
        when(categoryService.getAllCategories()).thenReturn(List.of());
        when(productService.getAllProducts(req)).thenReturn(List.of());
        when(recommendedService.getAllRecommendedProducts()).thenReturn(List.of());

        // Act
        ResponseEntity<Map<String, Object>> response = homeController.getHomeInfo(req);

        // Assert
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body).containsKeys("results", "recommendedProducts");

        assertThat((List<?>) body.get("results")).isEmpty();
        assertThat((List<?>) body.get("recommendedProducts")).isEmpty();
    }
}