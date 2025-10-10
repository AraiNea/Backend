package com.example.pizza_backend.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartItemTest {

    @Test
    void testBuilderAndGetters() {
        Cart cart = Cart.builder().cartId(1L).build();
        Product product = Product.builder().productId(10L).build();

        CartItem item = CartItem.builder()
                .cartItemId(100L)
                .cart(cart)
                .product(product)
                .qty(3)
                .lineTotal(300)
                .build();

        assertThat(item.getCartItemId()).isEqualTo(100L);
        assertThat(item.getCart()).isEqualTo(cart);
        assertThat(item.getProduct()).isEqualTo(product);
        assertThat(item.getQty()).isEqualTo(3);
        assertThat(item.getLineTotal()).isEqualTo(300);
    }

    @Test
    void testSetterAndGetters() {
        CartItem item = new CartItem();

        Cart cart = Cart.builder().cartId(2L).build();
        Product product = Product.builder().productId(20L).build();

        item.setCartItemId(101L);
        item.setCart(cart);
        item.setProduct(product);
        item.setQty(5);
        item.setLineTotal(500);

        assertThat(item.getCartItemId()).isEqualTo(101L);
        assertThat(item.getCart()).isEqualTo(cart);
        assertThat(item.getProduct()).isEqualTo(product);
        assertThat(item.getQty()).isEqualTo(5);
        assertThat(item.getLineTotal()).isEqualTo(500);
    }

    @Test
    void testEqualsAndHashCode() {
        CartItem item1 = CartItem.builder().cartItemId(1L).build();
        CartItem item2 = CartItem.builder().cartItemId(1L).build();

        assertThat(item1).isEqualTo(item2);
        assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
    }

    @Test
    void testToString() {
        CartItem item = CartItem.builder()
                .cartItemId(1L)
                .qty(2)
                .lineTotal(200)
                .build();

        String str = item.toString();
        assertThat(str).contains("cartItemId=1", "qty=2", "lineTotal=200");
    }
}
