package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.FileUploadUtil;
import com.example.pizza_backend.api.dto.RecommendedProductDto;
import com.example.pizza_backend.api.dto.input.RecommendedInput;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.exception.IdNotFoundException;
import com.example.pizza_backend.persistence.entity.Product;
import com.example.pizza_backend.persistence.entity.RecommendedProduct;
import com.example.pizza_backend.persistence.repository.ProductRepository;
import com.example.pizza_backend.persistence.repository.RecommendedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RecommendedServiceImplTest {

    @InjectMocks
    private RecommendedServiceImpl recommendedService;

    @Mock
    private RecommendedRepository recommendedRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Mapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRecommendedProducts_shouldReturnDtoList() {
        // given: entity
        Product product1 = new Product();
        product1.setProductId(101L);
        Product product2 = new Product();
        product2.setProductId(102L);

        RecommendedProduct rec1 = new RecommendedProduct();
        rec1.setRecommendedId(1L);
        rec1.setProduct(product1);
        rec1.setRecommendedImg("pizza.png");

        RecommendedProduct rec2 = new RecommendedProduct();
        rec2.setRecommendedId(2L);
        rec2.setProduct(product2);
        rec2.setRecommendedImg("pasta.png");

        // DTO
        RecommendedProductDto dto1 = RecommendedProductDto.builder()
                .recommendedId(rec1.getRecommendedId())
                .productId(rec1.getProduct().getProductId())
                .recommendImgPath("/Images/recommended-photos/" + rec1.getRecommendedImg())
                .build();

        RecommendedProductDto dto2 = RecommendedProductDto.builder()
                .recommendedId(rec2.getRecommendedId())
                .productId(rec2.getProduct().getProductId())
                .recommendImgPath("/Images/recommended-photos/" + rec2.getRecommendedImg())
                .build();

        // mock repository + mapper
        when(recommendedRepository.findAll()).thenReturn(List.of(rec1, rec2));
        when(mapper.toRecommendedProductDto(rec1)).thenReturn(dto1);
        when(mapper.toRecommendedProductDto(rec2)).thenReturn(dto2);

        // when
        List<RecommendedProductDto> result = recommendedService.getAllRecommendedProducts();

        // then
        assertThat(result).containsExactly(dto1, dto2);

        verify(recommendedRepository).findAll();
        verify(mapper).toRecommendedProductDto(rec1);
        verify(mapper).toRecommendedProductDto(rec2);
    }





    @Test
    void createRecommended_shouldReturnSuccess() throws IOException {
        RecommendedInput input = new RecommendedInput();
        input.setProductId(1L);

        Product product = new Product();
        product.setProductImg("pizza.png");
        RecommendedProduct recommendedProduct = new RecommendedProduct();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(mapper.toRecommendedProduct(input)).thenReturn(recommendedProduct);

        // Mock static Files.copy to avoid accessing real file
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.copy(
                    any(Path.class),
                    any(Path.class),
                    any(StandardCopyOption.class)
            )).thenReturn(null); // return null just to skip actual copy

            String result = recommendedService.createRecommended(input);
            assertThat(result).isEqualTo("success");

            filesMock.verify(() -> Files.copy(
                    Path.of("Images/product-photos/pizza.png"),
                    Path.of("Images/recommended-photos/pizza.png"),
                    StandardCopyOption.REPLACE_EXISTING
            ));
        }

        verify(recommendedRepository).save(recommendedProduct);
    }

    @Test
    void createRecommended_shouldThrowException_whenProductIdIsNull() {
        RecommendedInput input = new RecommendedInput();
        input.setProductId(null);

        assertThrows(IllegalArgumentException.class, () -> recommendedService.createRecommended(input));
    }

    @Test
    void createRecommended_shouldThrowException_whenProductNotFound() {
        RecommendedInput input = new RecommendedInput();
        input.setProductId(99L);

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> recommendedService.createRecommended(input));
    }

    @Test
    void deleteRecommended_shouldReturnSuccess() throws IOException {
        RecommendedInput input = new RecommendedInput();
        input.setRecommendedId(1L);

        RecommendedProduct recProduct = new RecommendedProduct();
        recProduct.setRecommendedImg("pizza.png");

        when(recommendedRepository.findById(1L)).thenReturn(Optional.of(recProduct));

        // Mock FileUploadUtil.deleteFile static method
        try (MockedStatic<FileUploadUtil> fileUtilMock = mockStatic(FileUploadUtil.class)) {
            String result = recommendedService.deleteRecommended(input);
            assertThat(result).isEqualTo("success");

            fileUtilMock.verify(() -> FileUploadUtil.deleteFile("Images/recommended-photos/", "pizza.png"));
        }

        verify(recommendedRepository).deleteById(1L);
    }

    @Test
    void deleteRecommended_shouldThrowException_whenRecommendedIdIsNull() {
        RecommendedInput input = new RecommendedInput();
        input.setRecommendedId(null);

        assertThrows(IllegalArgumentException.class, () -> recommendedService.deleteRecommended(input));
    }

    @Test
    void deleteRecommended_shouldThrowException_whenRecommendedNotFound() {
        RecommendedInput input = new RecommendedInput();
        input.setRecommendedId(99L);

        when(recommendedRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> recommendedService.deleteRecommended(input));
    }
}
