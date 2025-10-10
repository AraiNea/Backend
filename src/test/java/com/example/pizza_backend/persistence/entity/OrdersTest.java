package com.example.pizza_backend.persistence.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class OrdersTest {

    @Test
    void testBuilderAndGetters() {
        Profile profile = Profile.builder().profileId(1L).build();
        Address address = Address.builder().addressId(10L).build();
        LocalDateTime now = LocalDateTime.now();

        Orders order = Orders.builder()
                .orderId(100L)
                .profile(profile)
                .address(address)
                .status(1)
                .subtotal(500)
                .deliveryFee(50)
                .grandTotal(550)
                .createdAt(now)
                .fulfilledAt(now.plusDays(1))
                .fulfilledBy("Admin")
                .build();

        assertThat(order.getOrderId()).isEqualTo(100L);
        assertThat(order.getProfile()).isEqualTo(profile);
        assertThat(order.getAddress()).isEqualTo(address);
        assertThat(order.getStatus()).isEqualTo(1);
        assertThat(order.getSubtotal()).isEqualTo(500);
        assertThat(order.getDeliveryFee()).isEqualTo(50);
        assertThat(order.getGrandTotal()).isEqualTo(550);
        assertThat(order.getCreatedAt()).isEqualTo(now);
        assertThat(order.getFulfilledAt()).isEqualTo(now.plusDays(1));
        assertThat(order.getFulfilledBy()).isEqualTo("Admin");
    }

    @Test
    void testSetterAndGetters() {
        Orders order = new Orders();
        Profile profile = Profile.builder().profileId(2L).build();
        Address address = Address.builder().addressId(20L).build();
        LocalDateTime now = LocalDateTime.of(2025, 10, 10, 12, 0);

        order.setOrderId(101L);
        order.setProfile(profile);
        order.setAddress(address);
        order.setStatus(2);
        order.setSubtotal(600);
        order.setDeliveryFee(60);
        order.setGrandTotal(660);
        order.setCreatedAt(now);
        order.setFulfilledAt(now.plusHours(2));
        order.setFulfilledBy("Staff");

        assertThat(order.getOrderId()).isEqualTo(101L);
        assertThat(order.getProfile()).isEqualTo(profile);
        assertThat(order.getAddress()).isEqualTo(address);
        assertThat(order.getStatus()).isEqualTo(2);
        assertThat(order.getSubtotal()).isEqualTo(600);
        assertThat(order.getDeliveryFee()).isEqualTo(60);
        assertThat(order.getGrandTotal()).isEqualTo(660);
        assertThat(order.getCreatedAt()).isEqualTo(now);
        assertThat(order.getFulfilledAt()).isEqualTo(now.plusHours(2));
        assertThat(order.getFulfilledBy()).isEqualTo("Staff");
    }

    @Test
    void testEqualsAndHashCode() {
        Orders o1 = Orders.builder().orderId(1L).build();
        Orders o2 = Orders.builder().orderId(1L).build();

        assertThat(o1).isEqualTo(o2);
        assertThat(o1.hashCode()).isEqualTo(o2.hashCode());
    }

    @Test
    void testToString() {
        Orders order = Orders.builder()
                .orderId(1L)
                .status(0)
                .grandTotal(1000)
                .build();

        String str = order.toString();
        assertThat(str).contains("orderId=1", "status=0", "grandTotal=1000");
    }
}
