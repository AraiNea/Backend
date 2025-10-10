package com.example.pizza_backend.persistence.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CartTest {

    @Test
    void testBuilderAndGetters() {
        LocalDate now = LocalDate.now();

        Cart cart = Cart.builder()
                .cartId(1L)
                .createdAt(now)
                .note("Test note")
                .build();

        assertThat(cart.getCartId()).isEqualTo(1L);
        assertThat(cart.getCreatedAt()).isEqualTo(now);
        assertThat(cart.getNote()).isEqualTo("Test note");
    }

    @Test
    void testSetterAndGetters() {
        Cart cart = new Cart();
        cart.setCartId(2L);
        cart.setCreatedAt(LocalDate.of(2025, 10, 10));
        cart.setNote("Another note");

        assertThat(cart.getCartId()).isEqualTo(2L);
        assertThat(cart.getCreatedAt()).isEqualTo(LocalDate.of(2025, 10, 10));
        assertThat(cart.getNote()).isEqualTo("Another note");
    }




    @Test
    void testEqualsAndHashCode() {
        Cart cart1 = Cart.builder().cartId(1L).build();
        Cart cart2 = Cart.builder().cartId(1L).build();

        assertThat(cart1).isEqualTo(cart2);
        assertThat(cart1.hashCode()).isEqualTo(cart2.hashCode());
    }

    @Test
    void testToString() {
        Cart cart = Cart.builder()
                .cartId(1L)
                .note("Sample")
                .build();

        String str = cart.toString();
        assertThat(str).contains("cartId=1", "note=Sample");
    }
}
