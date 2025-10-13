package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.FileUploadUtil;
import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.input.ProductInput;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.exception.IdNotFoundException;
import com.example.pizza_backend.persistence.entity.Category;
import com.example.pizza_backend.persistence.entity.Product;
import com.example.pizza_backend.persistence.repository.CategoryRepository;
import com.example.pizza_backend.persistence.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private Mapper mapper;

    @Mock
    private MultipartFile imageFile;

    @InjectMocks
    private ProductServiceImpl productService;

    private Category category;
    private Product product;
    private ProductInput productInput;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setCategoryId(1L);

        product = new Product();
        product.setProductId(100L);
        product.setProductName("Pizza");
        product.setProductPrice(199);
        product.setProductStock(10);
        product.setProductDetail("Cheese Pizza");
        product.setProductImg("pizza.jpg");
        product.setCategory(category);

        productInput = new ProductInput();
        productInput.setProductId(100L);
        productInput.setCategoryId(1L);
        productInput.setProductName("Pizza");
    }

    // ---------- getAllProducts ----------
    @Test
    void getAllProducts_shouldReturnListOfProductDto() {
        ProductSearchReq req = new ProductSearchReq();
        req.setProductName("Pizza");

        Product product = new Product();
        product.setProductName("Pizza");
        product.setProductPrice(199);

        // สร้าง mock ProductDto แทน
        ProductDto productDto = mock(ProductDto.class);
        when(productDto.getProductName()).thenReturn("Pizza");
        when(productDto.getProductPrice()).thenReturn(199);

        // mock repository
        when(productRepository.searchProducts(
                null, "Pizza", null, null, null, null
        )).thenReturn(List.of(product));

        // mock mapper ให้ return mock ProductDto
        when(mapper.toProductDto(product)).thenReturn(productDto);

        List<ProductDto> result = productService.getAllProducts(req);

        assertEquals(1, result.size());
        assertEquals("Pizza", result.get(0).getProductName());
        assertEquals(199.0, result.get(0).getProductPrice().doubleValue());

        verify(productRepository, times(1)).searchProducts(null, "Pizza", null, null, null, null);
    }


    // ---------- createProduct ----------
    @Test
    void createProduct_shouldCreateSuccessfully() throws IOException {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(mapper.toProduct(productInput, "admin")).thenReturn(product);
        when(imageFile.getOriginalFilename()).thenReturn("pizza.jpg");

        try (MockedStatic<FileUploadUtil> util = mockStatic(FileUploadUtil.class)) {
            String result = productService.createProduct(productInput, imageFile, "admin");

            assertEquals("success", result);
            verify(productRepository).save(product);
            util.verify(() -> FileUploadUtil.saveFile(anyString(), any(), anyString()), times(1));
        }
    }

    @Test
    void createProduct_shouldThrowException_whenCategoryIdNull() {
        productInput.setCategoryId(null);
        assertThrows(IllegalArgumentException.class,
                () -> productService.createProduct(productInput, imageFile, "admin"));
    }

    @Test
    void createProduct_shouldThrowException_whenCategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IdNotFoundException.class,
                () -> productService.createProduct(productInput, imageFile, "admin"));
    }

    // ---------- updateProduct ----------
    @Test
    void updateProduct_shouldUpdateSuccessfully() throws IOException {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageFile.getOriginalFilename()).thenReturn("new_pizza.jpg");

        try (MockedStatic<FileUploadUtil> util = mockStatic(FileUploadUtil.class)) {
            String result = productService.updateProduct(productInput, imageFile, "admin");

            assertEquals("success", result);
            verify(mapper).updateProductFromInput(productInput, product, "admin");
            verify(productRepository).save(product);
            util.verify(() -> FileUploadUtil.deleteFile(anyString(), anyString()), times(1));
            util.verify(() -> FileUploadUtil.saveFile(anyString(), any(), anyString()), times(1));
        }
    }

    @Test
    void updateProduct_shouldThrowException_whenProductIdNull() {
        productInput.setProductId(null);
        assertThrows(IllegalArgumentException.class,
                () -> productService.updateProduct(productInput, imageFile, "admin"));
    }

    @Test
    void updateProduct_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(IdNotFoundException.class,
                () -> productService.updateProduct(productInput, imageFile, "admin"));
    }

    // ---------- deleteProduct ----------
    @Test
    void deleteProduct_shouldDeleteSuccessfully() throws IOException {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        try (MockedStatic<FileUploadUtil> util = mockStatic(FileUploadUtil.class)) {
            String result = productService.deleteProduct(productInput);

            assertEquals("success", result);
            verify(productRepository).deleteById(100L);
            util.verify(() -> FileUploadUtil.deleteFile(anyString(), eq("pizza.jpg")), times(1));
        }
    }

    @Test
    void deleteProduct_shouldThrowException_whenProductIdNull() {
        productInput.setProductId(null);
        assertThrows(IllegalArgumentException.class,
                () -> productService.deleteProduct(productInput));
    }

    @Test
    void deleteProduct_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(IdNotFoundException.class,
                () -> productService.deleteProduct(productInput));
    }
}
