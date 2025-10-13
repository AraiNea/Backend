package com.example.pizza_backend.api.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CategoryDtoTest {

    @Test
    void testBuilderAndGetters() {
        CategoryDto dto = CategoryDto.builder()
                .categoryId(1L)
                .categoryName("Pizza")
                .categoryImgPath("pizza.png")
                .categoryProductPath("/products/pizza")
                .categoryPriority(10L)
                .build();

        assertThat(dto.getCategoryId()).isEqualTo(1L);
        assertThat(dto.getCategoryName()).isEqualTo("Pizza");
        assertThat(dto.getCategoryImgPath()).isEqualTo("pizza.png");
        assertThat(dto.getCategoryProductPath()).isEqualTo("/products/pizza");
        assertThat(dto.getCategoryPriority()).isEqualTo(10L);
    }

    @Test
    void testBuilderWithDifferentValues() {
        CategoryDto dto = CategoryDto.builder()
                .categoryId(2L)
                .categoryName("Burger")
                .categoryImgPath("burger.png")
                .categoryProductPath("/products/burger")
                .categoryPriority(5L)
                .build();

        assertThat(dto.getCategoryId()).isEqualTo(2L);
        assertThat(dto.getCategoryName()).isEqualTo("Burger");
        assertThat(dto.getCategoryImgPath()).isEqualTo("burger.png");
        assertThat(dto.getCategoryProductPath()).isEqualTo("/products/burger");
        assertThat(dto.getCategoryPriority()).isEqualTo(5L);
    }

    @Test
    void testEqualsAndHashCode() {
        CategoryDto dto1 = CategoryDto.builder().categoryId(1L).categoryName("Pizza").build();
        CategoryDto dto2 = CategoryDto.builder().categoryId(1L).categoryName("Pizza").build();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void testToString() {
        CategoryDto dto = CategoryDto.builder()
                .categoryId(1L)
                .categoryName("Pizza")
                .build();

        String str = dto.toString();
        assertThat(str).contains("categoryId=1", "categoryName=Pizza");
    }
}
