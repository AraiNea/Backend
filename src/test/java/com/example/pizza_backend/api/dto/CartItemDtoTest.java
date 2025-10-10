package com.example.pizza_backend.api.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartItemDtoTest {

    @Test
    void testBuilderAndGetters() {
        CartItemDto dto = CartItemDto.builder()
                .cartItemId(1L)
                .productId(100L)
                .productName("Cheese Pizza")
                .productDetail("Delicious pizza with cheese")
                .productPrice(250)
                .qty(2)
                .lineTotal(500)
                .build();

        assertThat(dto.getCartItemId()).isEqualTo(1L);
        assertThat(dto.getProductId()).isEqualTo(100L);
        assertThat(dto.getProductName()).isEqualTo("Cheese Pizza");
        assertThat(dto.getProductDetail()).isEqualTo("Delicious pizza with cheese");
        assertThat(dto.getProductPrice()).isEqualTo(250);
        assertThat(dto.getQty()).isEqualTo(2);
        assertThat(dto.getLineTotal()).isEqualTo(500);
    }

    @Test
    void testEqualsAndHashCode() {
        CartItemDto dto1 = CartItemDto.builder().cartItemId(1L).build();
        CartItemDto dto2 = CartItemDto.builder().cartItemId(1L).build();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void testToString() {
        CartItemDto dto = CartItemDto.builder()
                .cartItemId(1L)
                .productName("Pizza")
                .productPrice(250)
                .build();

        String str = dto.toString();
        assertThat(str).contains("cartItemId=1", "productName=Pizza", "productPrice=250");
    }
}
