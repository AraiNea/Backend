package com.example.pizza_backend.api.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecommendedProductDtoTest {

    @Test
    void testBuilderAndGetters() {
        RecommendedProductDto dto = RecommendedProductDto.builder()
                .recommendedId(1L)
                .productId(100L)
                .recommendImgPath("recommended.png")
                .build();

        assertThat(dto.getRecommendedId()).isEqualTo(1L);
        assertThat(dto.getProductId()).isEqualTo(100L);
        assertThat(dto.getRecommendImgPath()).isEqualTo("recommended.png");
    }

    @Test
    void testEqualsAndHashCode() {
        RecommendedProductDto dto1 = RecommendedProductDto.builder()
                .recommendedId(1L)
                .productId(100L)
                .recommendImgPath("img1.png")
                .build();

        RecommendedProductDto dto2 = RecommendedProductDto.builder()
                .recommendedId(1L)
                .productId(100L)
                .recommendImgPath("img1.png")
                .build();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void testToString() {
        RecommendedProductDto dto = RecommendedProductDto.builder()
                .recommendedId(1L)
                .recommendImgPath("img.png")
                .build();

        String str = dto.toString();
        assertThat(str).contains("recommendedId=1", "recommendImgPath=img.png");
    }
}
