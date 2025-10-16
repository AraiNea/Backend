package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.CategoryDto;
import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.RecommendedProductDto;
import com.example.pizza_backend.api.dto.input.ProductInput;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.service.CategoryService;
import com.example.pizza_backend.service.ProductService;
import com.example.pizza_backend.service.RecommendedService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private RecommendedService recommendedService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private MultipartFile imageFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProducts_shouldReturnProductsAndCategories() {
        ProductSearchReq searchReq = new ProductSearchReq();

        // สร้าง ProductDto ด้วย builder (ยังทำได้)
        ProductDto product1 = ProductDto.builder()
                .categoryId(1L)
                .categoryName("Pizza")
                .productId(101L)
                .productName("Margherita")
                .productDetail("Cheese pizza")
                .productPrice(250)
                .productStock(10)
                .productImgPath("/images/margherita.png")
                .isActive(1)
                .createdAt(LocalDate.now())
                .createdBy("admin")
                .build();

        // mock RecommendedProductDto
        RecommendedProductDto rec1 = mock(RecommendedProductDto.class);
        when(rec1.getProductId()).thenReturn(101L);

        // mock CategoryDto
        CategoryDto category1 = mock(CategoryDto.class);
        when(category1.getCategoryId()).thenReturn(1L);
        when(category1.getCategoryName()).thenReturn("Pizza");

        // mock service
        when(productService.getAllProducts(searchReq)).thenReturn(List.of(product1));
        when(recommendedService.getAllRecommendedProducts()).thenReturn(List.of(rec1));
        when(categoryService.getAllCategories()).thenReturn(List.of(category1));

        // เรียก controller
        ResponseEntity<Map<String, Object>> response = productController.getAllProducts(searchReq);
        Map<String, Object> body = response.getBody();

        // assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("products")).isEqualTo(List.of(product1));

        // แปลง recommendedProductDto เป็น list ของ productId ก่อน assert
        List<Long> recommendedIds = List.of(101L);
        List<Long> actualIds = ((List<RecommendedProductDto>) body.get("recommendProductId")).stream()
                .map(RecommendedProductDto::getProductId)
                .toList();
        assertThat(actualIds).isEqualTo(recommendedIds);

        // แปลง categoryDto เป็น list ของ categoryId ก่อน assert (ตัวอย่างง่าย ๆ)
        List<Long> expectedCategoryIds = List.of(1L);
        List<Long> actualCategoryIds = ((List<CategoryDto>) body.get("categoriesDropdown")).stream()
                .map(CategoryDto::getCategoryId)
                .toList();
        assertThat(actualCategoryIds).isEqualTo(expectedCategoryIds);

        // verify ว่า service ถูกเรียก
        verify(productService).getAllProducts(searchReq);
        verify(recommendedService).getAllRecommendedProducts();
        verify(categoryService).getAllCategories();
    }


    @Test
    void createProduct_shouldReturnSuccess() throws IOException {
        when(request.getAttribute("username")).thenReturn("admin");
        ProductInput input = new ProductInput();

        when(productService.createProduct(input, imageFile, "admin")).thenReturn("success");

        ResponseEntity<?> response = productController.createProduct(request, input, imageFile);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("message")).isEqualTo("create success");

        verify(productService).createProduct(input, imageFile, "admin");
    }

    @Test
    void createProduct_shouldReturnBadRequest_whenServiceFails() throws IOException {
        when(request.getAttribute("username")).thenReturn("admin");
        ProductInput input = new ProductInput();

        when(productService.createProduct(input, imageFile, "admin")).thenReturn("error");

        ResponseEntity<?> response = productController.createProduct(request, input, imageFile);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        verify(productService).createProduct(input, imageFile, "admin");
    }

    @Test
    void updateProduct_shouldReturnSuccess_withImage() throws IOException {
        when(request.getAttribute("username")).thenReturn("admin");
        ProductInput input = new ProductInput();
        when(imageFile.isEmpty()).thenReturn(false);

        when(productService.updateProduct(input, imageFile, "admin")).thenReturn("success");

        ResponseEntity<?> response = productController.updateProduct(request, input, imageFile);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("message")).isEqualTo("update success");

        verify(productService).updateProduct(input, imageFile, "admin");
    }

    @Test
    void updateProduct_shouldReturnSuccess_withoutImage() throws IOException {
        when(request.getAttribute("username")).thenReturn("admin");
        ProductInput input = new ProductInput();

        when(productService.updateProduct(input, null, "admin")).thenReturn("success");

        ResponseEntity<?> response = productController.updateProduct(request, input, null);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("message")).isEqualTo("update success");

        verify(productService).updateProduct(input, null, "admin");
    }

    @Test
    void deleteProduct_shouldReturnSuccess() throws IOException {
        ProductInput input = new ProductInput();
        when(productService.deleteProduct(input)).thenReturn("success");

        ResponseEntity<?> response = productController.deleteProduct(input);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("message")).isEqualTo("delete success");

        verify(productService).deleteProduct(input);
    }

    @Test
    void deleteProduct_shouldReturnBadRequest_whenServiceFails() throws IOException {
        ProductInput input = new ProductInput();
        when(productService.deleteProduct(input)).thenReturn("error");

        ResponseEntity<?> response = productController.deleteProduct(input);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        verify(productService).deleteProduct(input);
    }
}
