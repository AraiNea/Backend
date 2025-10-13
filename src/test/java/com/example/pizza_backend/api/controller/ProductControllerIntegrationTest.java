package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.input.ProductInput;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private RecommendedService recommendedService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testGetAllProducts() throws Exception {
        // ใช้ mock ProductDto แทน constructor
        ProductDto mockProduct = mock(ProductDto.class);
        when(mockProduct.getProductId()).thenReturn(1L);
        when(mockProduct.getProductName()).thenReturn("Pepperoni");

        when(productService.getAllProducts(any(ProductSearchReq.class)))
                .thenReturn(List.of(mockProduct));
        when(recommendedService.getAllRecommendedProducts()).thenReturn(List.of());
        when(categoryService.getAllCategories()).thenReturn(List.of());

        mockMvc.perform(get("/product/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].productName").value("Pepperoni"));
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductInput productInput = new ProductInput();
        productInput.setProductName("Pepperoni");
        productInput.setProductPrice(250);
        productInput.setProductStock(15);
        productInput.setCategoryId(1L);
        productInput.setIsActive(1);

        MockMultipartFile productFile = new MockMultipartFile(
                "product",
                "",
                "application/json",
                ("{\"productName\":\"Pepperoni\",\"productPrice\":250}").getBytes()
        );

        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "pepperoni.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy image content".getBytes()
        );

        when(productService.createProduct(any(ProductInput.class), any(), anyString()))
                .thenReturn("success");

        mockMvc.perform(multipart("/product/create")
                        .file(productFile)
                        .file(imageFile)
                        .requestAttr("username", "admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("create success"));
    }

    @Test
    void testUpdateProduct_withImage() throws Exception {
        ProductInput productInput = new ProductInput();
        productInput.setProductId(1L);
        productInput.setProductName("Pepperoni Updated");
        productInput.setProductPrice(300);
        productInput.setProductStock(20);
        productInput.setCategoryId(1L);
        productInput.setIsActive(1);

        MockMultipartFile productFile = new MockMultipartFile(
                "product",
                "",
                "application/json",
                ("{\"productId\":1,\"productName\":\"Pepperoni Updated\"}").getBytes()
        );

        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "pepperoni_updated.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy updated image content".getBytes()
        );

        when(productService.updateProduct(any(ProductInput.class), any(), anyString()))
                .thenReturn("success");

        mockMvc.perform(multipart("/product/update")
                        .file(productFile)
                        .file(imageFile)
                        .requestAttr("username", "admin")
                        .with(request -> { request.setMethod("POST"); return request; })) // multipart default is POST
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("update success"));
    }

    @Test
    void testUpdateProduct_withoutImage() throws Exception {
        ProductInput productInput = new ProductInput();
        productInput.setProductId(1L);
        productInput.setProductName("Pepperoni Updated");
        productInput.setProductPrice(300);

        MockMultipartFile productFile = new MockMultipartFile(
                "product",
                "",
                "application/json",
                ("{\"productId\":1,\"productName\":\"Pepperoni Updated\"}").getBytes()
        );

        when(productService.updateProduct(any(ProductInput.class), isNull(), anyString()))
                .thenReturn("success");

        mockMvc.perform(multipart("/product/update")
                        .file(productFile)
                        .requestAttr("username", "admin")
                        .with(request -> { request.setMethod("POST"); return request; }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("update success"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        ProductInput productInput = new ProductInput();
        productInput.setProductId(1L);

        when(productService.deleteProduct(any(ProductInput.class))).thenReturn("success");

        mockMvc.perform(post("/product/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("delete success"));
    }

}
