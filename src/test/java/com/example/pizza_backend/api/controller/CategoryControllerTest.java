package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.CategoryDto;
import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.input.CategoryInput;
import com.example.pizza_backend.api.dto.search.CategorySearchReq;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.service.CategoryService;
import com.example.pizza_backend.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductService productService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_shouldReturnBadRequest_whenServiceFails() throws IOException {
        CategoryInput input = new CategoryInput();
        input.setCategoryName("Pizza");

        MultipartFile file = new MockMultipartFile(
                "image", "pizza.png", "image/png", "dummy content".getBytes());

        when(request.getAttribute("username")).thenReturn("admin");
        when(categoryService.createCategory(any(CategoryInput.class), any(MultipartFile.class), anyString()))
                .thenReturn("error");

        ResponseEntity<?> response = categoryController.createProduct(request, input, file);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        verify(categoryService).createCategory(eq(input), eq(file), eq("admin"));
    }

    @Test
    void createProduct_shouldReturnOk_whenServiceSucceeds() throws IOException {
        CategoryInput input = new CategoryInput();
        input.setCategoryName("Pizza");

        MultipartFile file = new MockMultipartFile(
                "image", "pizza.png", "image/png", "dummy content".getBytes());

        when(request.getAttribute("username")).thenReturn("admin");
        when(categoryService.createCategory(any(), any(), anyString())).thenReturn("success");

        ResponseEntity<?> response = categoryController.createProduct(request, input, file);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(((Map<?, ?>) response.getBody()).get("message")).isEqualTo("create success");
        verify(categoryService).createCategory(eq(input), eq(file), eq("admin"));
    }

    @Test
    void updateProduct_withFile_shouldReturnOk_whenServiceSucceeds() throws IOException {
        CategoryInput input = new CategoryInput();
        input.setCategoryName("Pizza");

        MultipartFile file = new MockMultipartFile(
                "image", "pizza.png", "image/png", "dummy content".getBytes());

        when(request.getAttribute("username")).thenReturn("admin");
        when(categoryService.updateCategory(any(), any(), anyString())).thenReturn("success");

        ResponseEntity<?> response = categoryController.updateProduct(request, input, file);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(categoryService).updateCategory(eq(input), eq(file), eq("admin"));
    }

    @Test
    void updateProduct_withoutFile_shouldReturnOk_whenServiceSucceeds() throws IOException {
        CategoryInput input = new CategoryInput();
        input.setCategoryName("Pizza");

        when(request.getAttribute("username")).thenReturn("admin");
        when(categoryService.updateCategory(any(), isNull(), anyString())).thenReturn("success");

        ResponseEntity<?> response = categoryController.updateProduct(request, input, null);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(categoryService).updateCategory(eq(input), isNull(), eq("admin"));
    }

    @Test
    void deleteProduct_shouldReturnOk_whenServiceSucceeds() throws IOException {
        CategoryInput input = new CategoryInput();
        input.setCategoryName("Pizza");

        when(categoryService.deleteCategory(any())).thenReturn("success");

        ResponseEntity<?> response = categoryController.deleteProduct(input);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(((Map<?, ?>) response.getBody()).get("message")).isEqualTo("delete success");
        verify(categoryService).deleteCategory(eq(input));
    }

    @Test
    void getAllCategories_shouldReturnCategoriesAndProducts() {
        ProductSearchReq req = new ProductSearchReq();

        // ใช้ builder แทน constructor
        CategoryDto category = CategoryDto.builder()
                .categoryId(1L)
                .categoryName("Pizza")
                .categoryImgPath("pizza.png")
                .categoryProductPath("/images")
                .categoryPriority(1L)
                .build();

        ProductDto product = ProductDto.builder()
                .productId(1L)
                .productName("Pepperoni")
                .productDetail("Spicy pizza")
                .productPrice(300)
                .productStock(10)
                .categoryId(1L)
                .categoryName("Pizza")
                .build();

        when(categoryService.getAllCategories()).thenReturn(List.of(category));
        when(productService.getAllProducts(req)).thenReturn(List.of(product));

        ResponseEntity<?> response = categoryController.getAllCategories(req);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("categories")).isNotNull();
        assertThat(body.get("products")).isNotNull();
    }


    @Test
    void getAllCategoriesOnly_shouldReturnCategories() {
        CategorySearchReq req = new CategorySearchReq();

        CategoryDto category = CategoryDto.builder()
                .categoryId(1L)
                .categoryName("Pizza")
                .categoryImgPath("pizza.png")
                .categoryProductPath("/images")
                .categoryPriority(1L)
                .build();

        when(categoryService.getAllCategories(req)).thenReturn(List.of(category));

        ResponseEntity<?> response = categoryController.getAllCategoriesOnly(req);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("categories")).isNotNull();
    }

}
