package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.CartDto;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.exception.IdNotFoundException;
import com.example.pizza_backend.persistence.entity.Cart;
import com.example.pizza_backend.persistence.entity.Profile;
import com.example.pizza_backend.persistence.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;
    private CartDto cartDto;

    @BeforeEach
    void setUp() {
        // สร้าง Cart ตัวอย่าง
        Profile profile = new Profile();
        profile.setProfileId(1L);
        profile.setUsername("testuser");

        cart = new Cart();
        cart.setCartId(100L);
        cart.setProfile(profile);

        // สร้าง CartDto ตัวอย่าง
        cartDto = CartDto.builder()
                .cartId(100L)
                .profileId(1L)
                .username("testuser")
                .createdAt(LocalDate.now())
                .note(null)
                .cartItems(null)
                .build();
    }

    @Test
    void getCartDtoByProfileId_shouldReturnCartDto_whenCartExists() {
        // Arrange
        when(cartRepository.findByProfileProfileId(1L)).thenReturn(Optional.of(cart));
        when(mapper.toCartDto(cart)).thenReturn(cartDto);

        // Act
        CartDto result = cartService.getCartDtoByProfileId(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCartId()).isEqualTo(100L);
        assertThat(result.getUsername()).isEqualTo("testuser");

        verify(cartRepository, times(1)).findByProfileProfileId(1L);
        verify(mapper, times(1)).toCartDto(cart);
    }

    @Test
    void getCartDtoByProfileId_shouldThrowException_whenCartNotFound() {
        // Arrange
        when(cartRepository.findByProfileProfileId(999L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IdNotFoundException.class, () -> cartService.getCartDtoByProfileId(999L));

        verify(cartRepository, times(1)).findByProfileProfileId(999L);
        verify(mapper, never()).toCartDto(any());
    }
}
