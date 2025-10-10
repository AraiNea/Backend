package com.example.pizza_backend.api.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ProductDtoTest {

    @Test
    void testBuilderAndGetters() {
        ProductDto dto = ProductDto.builder()
                .categoryId(1L)
                .categoryName("Pizza")
                .productId(100L)
                .productName("Cheese Pizza")
                .productDetail("Delicious pizza with cheese")
                .productPrice(250)
                .productStock(10)
                .productImgPath("/images/pizza.png")
                .isActive(1)
                .createdAt(LocalDate.of(2025,10,10))
                .createdBy("admin")
                .updatedAt(LocalDate.of(2025,10,11))
                .updatedBy("admin")
                .build();

        assertThat(dto.getCategoryId()).isEqualTo(1L);
        assertThat(dto.getCategoryName()).isEqualTo("Pizza");
        assertThat(dto.getProductId()).isEqualTo(100L);
        assertThat(dto.getProductName()).isEqualTo("Cheese Pizza");
        assertThat(dto.getProductDetail()).isEqualTo("Delicious pizza with cheese");
        assertThat(dto.getProductPrice()).isEqualTo(250);
        assertThat(dto.getProductStock()).isEqualTo(10);
        assertThat(dto.getProductImgPath()).isEqualTo("/images/pizza.png");
        assertThat(dto.getIsActive()).isEqualTo(1);
        assertThat(dto.getCreatedAt()).isEqualTo(LocalDate.of(2025,10,10));
        assertThat(dto.getCreatedBy()).isEqualTo("admin");
        assertThat(dto.getUpdatedAt()).isEqualTo(LocalDate.of(2025,10,11));
        assertThat(dto.getUpdatedBy()).isEqualTo("admin");
    }

    @Test
    void testEqualsAndHashCode() {
        ProductDto dto1 = ProductDto.builder().productId(100L).build();
        ProductDto dto2 = ProductDto.builder().productId(100L).build();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void testToString() {
        ProductDto dto = ProductDto.builder()
                .productId(100L)
                .productName("Cheese Pizza")
                .build();

        String str = dto.toString();
        assertThat(str).contains("productId=100", "productName=Cheese Pizza");
    }
}
