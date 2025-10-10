package com.example.pizza_backend.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

    @Test
    void testBuilderAndGetters() {
        Category category = Category.builder()
                .categoryId(1L)
                .categoryName("Pizza")
                .categoryImg("pizza.png")
                .categoryProductPath("/products/pizza")
                .categoryPriority(10L)
                .build();

        assertThat(category.getCategoryId()).isEqualTo(1L);
        assertThat(category.getCategoryName()).isEqualTo("Pizza");
        assertThat(category.getCategoryImg()).isEqualTo("pizza.png");
        assertThat(category.getCategoryProductPath()).isEqualTo("/products/pizza");
        assertThat(category.getCategoryPriority()).isEqualTo(10L);
    }

    @Test
    void testSetterAndGetters() {
        Category category = new Category();
        category.setCategoryId(2L);
        category.setCategoryName("Burger");
        category.setCategoryImg("burger.png");
        category.setCategoryProductPath("/products/burger");
        category.setCategoryPriority(20L);

        assertThat(category.getCategoryId()).isEqualTo(2L);
        assertThat(category.getCategoryName()).isEqualTo("Burger");
        assertThat(category.getCategoryImg()).isEqualTo("burger.png");
        assertThat(category.getCategoryProductPath()).isEqualTo("/products/burger");
        assertThat(category.getCategoryPriority()).isEqualTo(20L);
    }

    @Test
    void testGetCategoryImgPath() {
        Category category = new Category();
        category.setCategoryImg("pizza.png");
        assertThat(category.getCategoryImgPath()).isEqualTo("/Images/category-photos/pizza.png");

        category.setCategoryImg(null);
        assertThat(category.getCategoryImgPath()).isNull();
    }

    @Test
    void testEqualsAndHashCode() {
        Category cat1 = Category.builder().categoryId(1L).build();
        Category cat2 = Category.builder().categoryId(1L).build();

        assertThat(cat1).isEqualTo(cat2);
        assertThat(cat1.hashCode()).isEqualTo(cat2.hashCode());
    }

    @Test
    void testToString() {
        Category category = Category.builder()
                .categoryId(1L)
                .categoryName("Pizza")
                .build();

        String str = category.toString();
        assertThat(str).contains("categoryId=1", "categoryName=Pizza");
    }
}
