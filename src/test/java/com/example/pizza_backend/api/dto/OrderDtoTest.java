package com.example.pizza_backend.api.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class OrderDtoTest {

    @Test
    void testBuilderAndGetters() {
        OrderDto dto = OrderDto.builder()
                .orderId(1L)
                .username("daraporn")
                .status(2)
                .subtotal(500)
                .deliveryFee(50)
                .grandTotal(550)
                .createdAt(LocalDate.of(2025, 10, 10))
                .build();

        assertThat(dto.getOrderId()).isEqualTo(1L);
        assertThat(dto.getUsername()).isEqualTo("daraporn");
        assertThat(dto.getStatus()).isEqualTo(2);
        assertThat(dto.getSubtotal()).isEqualTo(500);
        assertThat(dto.getDeliveryFee()).isEqualTo(50);
        assertThat(dto.getGrandTotal()).isEqualTo(550);
        assertThat(dto.getCreatedAt()).isEqualTo(LocalDate.of(2025, 10, 10));
    }

    @Test
    void testEqualsAndHashCode() {
        OrderDto dto1 = OrderDto.builder().orderId(1L).build();
        OrderDto dto2 = OrderDto.builder().orderId(1L).build();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void testToString() {
        OrderDto dto = OrderDto.builder()
                .orderId(1L)
                .username("daraporn")
                .build();

        String str = dto.toString();
        assertThat(str).contains("orderId=1", "username=daraporn");
    }
}
