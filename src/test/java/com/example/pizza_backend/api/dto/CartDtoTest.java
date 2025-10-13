package com.example.pizza_backend.api.dto;

import com.example.pizza_backend.persistence.entity.CartItem;
//package com.example.pizza_backend.api.dto;

import com.example.pizza_backend.persistence.entity.CartItem;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CartDtoTest {

    @Test
    void testBuilderAndGetters() {
        CartItem item1 = new CartItem();
        CartItem item2 = new CartItem();
        List<CartItem> items = List.of(item1, item2);

        CartDto dto = CartDto.builder()
                .cartId(1L)
                .profileId(10L)
                .username("daraporn")
                .createdAt(LocalDate.of(2025,10,10))
                .note("Test note")
                .cartItems(items)
                .build();

        assertThat(dto.getCartId()).isEqualTo(1L);
        assertThat(dto.getProfileId()).isEqualTo(10L);
        assertThat(dto.getUsername()).isEqualTo("daraporn");
        assertThat(dto.getCreatedAt()).isEqualTo(LocalDate.of(2025,10,10));
        assertThat(dto.getNote()).isEqualTo("Test note");
        assertThat(dto.getCartItems()).hasSize(2).containsExactly(item1, item2);
    }

    @Test
    void testSetterAndGetters() {
        CartDto dto = CartDto.builder()
                .cartId(2L)
                .profileId(20L)
                .username("somchai")
                .createdAt(LocalDate.of(2025,1,1))
                .note("Another note")
                .cartItems(new ArrayList<>())
                .build();

        assertThat(dto.getCartId()).isEqualTo(2L);
        assertThat(dto.getProfileId()).isEqualTo(20L);
        assertThat(dto.getUsername()).isEqualTo("somchai");
        assertThat(dto.getCreatedAt()).isEqualTo(LocalDate.of(2025,1,1));
        assertThat(dto.getNote()).isEqualTo("Another note");
        assertThat(dto.getCartItems()).isEmpty();
    }

    @Test
    void testEqualsAndHashCode() {
        CartDto dto1 = CartDto.builder().cartId(1L).build();
        CartDto dto2 = CartDto.builder().cartId(1L).build();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void testToString() {
        CartDto dto = CartDto.builder()
                .cartId(1L)
                .username("daraporn")
                .note("note")
                .build();

        String str = dto.toString();
        assertThat(str).contains("cartId=1", "username=daraporn", "note=note");
    }
}
