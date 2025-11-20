package com.example.pizza_backend.api.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
                .createdAt(LocalDateTime.of(2025,10,10,0,0))
                .createdBy("admin")
                .updatedAt(LocalDateTime.of(2025,10,11,0,0))
                .updatedBy("admin")
                .build();

        assertThat(dto.getCreatedAt()).isEqualTo(LocalDateTime.of(2025,10,10,0,0));
        assertThat(dto.getUpdatedAt()).isEqualTo(LocalDateTime.of(2025,10,11,0,0));
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
