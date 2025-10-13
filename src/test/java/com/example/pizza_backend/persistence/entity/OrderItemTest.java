package com.example.pizza_backend.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemTest {

    @Test
    void testBuilderAndGetters() {
        Orders order = Orders.builder().orderId(1L).build();

        OrderItem item = OrderItem.builder()
                .orderItemId(100L)
                .order(order)
                .productIdSnapshot(10L)
                .productName("Pizza")
                .productDetail("Cheese Pizza")
                .productPrice(250)
                .qty(2)
                .lineTotal(500)
                .build();

        assertThat(item.getOrderItemId()).isEqualTo(100L);
        assertThat(item.getOrder()).isEqualTo(order);
        assertThat(item.getProductIdSnapshot()).isEqualTo(10L);
        assertThat(item.getProductName()).isEqualTo("Pizza");
        assertThat(item.getProductDetail()).isEqualTo("Cheese Pizza");
        assertThat(item.getProductPrice()).isEqualTo(250);
        assertThat(item.getQty()).isEqualTo(2);
        assertThat(item.getLineTotal()).isEqualTo(500);
    }

    @Test
    void testSetterAndGetters() {
        OrderItem item = new OrderItem();
        Orders order = Orders.builder().orderId(2L).build();

        item.setOrderItemId(101L);
        item.setOrder(order);
        item.setProductIdSnapshot(20L);
        item.setProductName("Burger");
        item.setProductDetail("Beef Burger");
        item.setProductPrice(150);
        item.setQty(3);
        item.setLineTotal(450);

        assertThat(item.getOrderItemId()).isEqualTo(101L);
        assertThat(item.getOrder()).isEqualTo(order);
        assertThat(item.getProductIdSnapshot()).isEqualTo(20L);
        assertThat(item.getProductName()).isEqualTo("Burger");
        assertThat(item.getProductDetail()).isEqualTo("Beef Burger");
        assertThat(item.getProductPrice()).isEqualTo(150);
        assertThat(item.getQty()).isEqualTo(3);
        assertThat(item.getLineTotal()).isEqualTo(450);
    }

    @Test
    void testEqualsAndHashCode() {
        OrderItem item1 = OrderItem.builder().orderItemId(1L).build();
        OrderItem item2 = OrderItem.builder().orderItemId(1L).build();

        assertThat(item1).isEqualTo(item2);
        assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
    }

    @Test
    void testToString() {
        OrderItem item = OrderItem.builder()
                .orderItemId(1L)
                .productName("Pizza")
                .qty(2)
                .lineTotal(500)
                .build();

        String str = item.toString();
        assertThat(str).contains("orderItemId=1", "productName=Pizza", "qty=2", "lineTotal=500");
    }
}
