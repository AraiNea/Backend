package com.example.pizza_backend.persistence.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void testBuilderAndGetters() {
        Category category = Category.builder().categoryId(1L).categoryName("Pizza").build();
        LocalDateTime now = LocalDateTime.now();

        Product product = Product.builder()
                .productId(100L)
                .category(category)
                .productName("Cheese Pizza")
                .productDetail("Delicious cheese pizza")
                .productImg("cheese.png")
                .productPrice(250)
                .productStock(10)
                .isActive(1)
                .createdAt(now)
                .createdBy("Admin")
                .updatedAt(now.plusHours(1))
                .updatedBy("Admin")
                .build();

        assertThat(product.getProductId()).isEqualTo(100L);
        assertThat(product.getCategory()).isEqualTo(category);
        assertThat(product.getProductName()).isEqualTo("Cheese Pizza");
        assertThat(product.getProductDetail()).isEqualTo("Delicious cheese pizza");
        assertThat(product.getProductImg()).isEqualTo("cheese.png");
        assertThat(product.getProductPrice()).isEqualTo(250);
        assertThat(product.getProductStock()).isEqualTo(10);
        assertThat(product.getIsActive()).isEqualTo(1);
        assertThat(product.getCreatedAt()).isEqualTo(now);
        assertThat(product.getCreatedBy()).isEqualTo("Admin");
        assertThat(product.getUpdatedAt()).isEqualTo(now.plusHours(1));
        assertThat(product.getUpdatedBy()).isEqualTo("Admin");
    }

    @Test
    void testSetterAndGetters() {
        Product product = new Product();
        Category category = Category.builder().categoryId(2L).categoryName("Burger").build();
        LocalDateTime now = LocalDateTime.of(2025, 10, 10, 12, 0);

        product.setProductId(101L);
        product.setCategory(category);
        product.setProductName("Beef Burger");
        product.setProductDetail("Juicy beef burger");
        product.setProductImg("beef.png");
        product.setProductPrice(150);
        product.setProductStock(20);
        product.setIsActive(1);
        product.setCreatedAt(now);
        product.setCreatedBy("Staff");
        product.setUpdatedAt(now.plusHours(2));
        product.setUpdatedBy("Staff");

        assertThat(product.getProductId()).isEqualTo(101L);
        assertThat(product.getCategory()).isEqualTo(category);
        assertThat(product.getProductName()).isEqualTo("Beef Burger");
        assertThat(product.getProductDetail()).isEqualTo("Juicy beef burger");
        assertThat(product.getProductImg()).isEqualTo("beef.png");
        assertThat(product.getProductPrice()).isEqualTo(150);
        assertThat(product.getProductStock()).isEqualTo(20);
        assertThat(product.getIsActive()).isEqualTo(1);
        assertThat(product.getCreatedAt()).isEqualTo(now);
        assertThat(product.getCreatedBy()).isEqualTo("Staff");
        assertThat(product.getUpdatedAt()).isEqualTo(now.plusHours(2));
        assertThat(product.getUpdatedBy()).isEqualTo("Staff");
    }



    @Test
    void testEqualsAndHashCode() {
        Product p1 = Product.builder().productId(1L).build();
        Product p2 = Product.builder().productId(1L).build();

        assertThat(p1).isEqualTo(p2);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }

    @Test
    void testToString() {
        Product product = Product.builder()
                .productId(1L)
                .productName("Pizza")
                .productPrice(250)
                .build();

        String str = product.toString();
        assertThat(str).contains("productId=1", "productName=Pizza", "productPrice=250");
    }
}
