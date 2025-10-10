package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.CartDto;
import com.example.pizza_backend.api.dto.CartItemDto;
import com.example.pizza_backend.api.dto.input.CartItemInput;
import com.example.pizza_backend.service.CartItemService;
import com.example.pizza_backend.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCart_shouldReturnCartAndItems() {
        when(request.getAttribute("profile_id")).thenReturn(1L);

        // ใช้ mock แทน package-private CartDto
        CartDto cartDto = mock(CartDto.class);
        when(cartDto.getCartId()).thenReturn(10L);

        // ใช้ mock แทน CartItemDto
        CartItemDto item1 = mock(CartItemDto.class);
        when(item1.getCartItemId()).thenReturn(100L);

        when(cartService.getCartDtoByProfileId(1L)).thenReturn(cartDto);
        when(cartItemService.getCartItemsByCartId(10L)).thenReturn(List.of(item1));

        ResponseEntity<?> response = cartController.getCart(request);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("cart")).isEqualTo(cartDto);
        assertThat(body.get("cartItems")).isEqualTo(List.of(item1));

        verify(cartService).getCartDtoByProfileId(1L);
        verify(cartItemService).getCartItemsByCartId(10L);
    }

    @Test
    void addItem_shouldReturnSuccessMessage() {
        when(request.getAttribute("profile_id")).thenReturn(1L);
        CartItemInput input = new CartItemInput();
        input.setProductId(101L);
        input.setQty(2);

        when(cartItemService.createCartItem(input, 1L)).thenReturn("success");

        ResponseEntity<?> response = cartController.addItem(request, input);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("message")).isEqualTo("Add items successfully");

        verify(cartItemService).createCartItem(input, 1L);
    }

    @Test
    void updateItem_shouldReturnSuccessMessage() {
        when(request.getAttribute("profile_id")).thenReturn(1L);
        CartItemInput input = new CartItemInput();
        input.setProductId(101L);
        input.setQty(5);

        when(cartItemService.updateCartItem(input, 1L)).thenReturn("success");

        ResponseEntity<?> response = cartController.updateItem(request, input);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("message")).isEqualTo("Update items successfully");

        verify(cartItemService).updateCartItem(input, 1L);
    }

    @Test
    void deleteItem_shouldReturnSuccessMessage() {
        when(request.getAttribute("profile_id")).thenReturn(1L);
        CartItemInput input = new CartItemInput();
        input.setCartItemId(100L);

        when(cartItemService.deleteCartItem(input, 1L)).thenReturn("success");

        ResponseEntity<?> response = cartController.deleteItem(request, input);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("message")).isEqualTo("Delete items successfully");

        verify(cartItemService).deleteCartItem(input, 1L);
    }

    // Optional: test failure cases (service return error)
    @Test
    void addItem_shouldReturnBadRequest_whenServiceFails() {
        when(request.getAttribute("profile_id")).thenReturn(1L);
        CartItemInput input = new CartItemInput();
        input.setProductId(101L);
        input.setQty(2);

        when(cartItemService.createCartItem(input, 1L)).thenReturn("error");

        ResponseEntity<?> response = cartController.addItem(request, input);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        verify(cartItemService).createCartItem(input, 1L);
    }

    @Test
    void updateItem_shouldReturnBadRequest_whenServiceFails() {
        when(request.getAttribute("profile_id")).thenReturn(1L);
        CartItemInput input = new CartItemInput();
        input.setProductId(101L);
        input.setQty(5);

        when(cartItemService.updateCartItem(input, 1L)).thenReturn("error");

        ResponseEntity<?> response = cartController.updateItem(request, input);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        verify(cartItemService).updateCartItem(input, 1L);
    }

    @Test
    void deleteItem_shouldReturnBadRequest_whenServiceFails() {
        when(request.getAttribute("profile_id")).thenReturn(1L);
        CartItemInput input = new CartItemInput();
        input.setCartItemId(100L);

        when(cartItemService.deleteCartItem(input, 1L)).thenReturn("error");

        ResponseEntity<?> response = cartController.deleteItem(request, input);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        verify(cartItemService).deleteCartItem(input, 1L);
    }
}
