package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.CartDto;
import com.example.pizza_backend.api.dto.CartItemDto;
import com.example.pizza_backend.api.dto.input.CartItemInput;
import com.example.pizza_backend.service.CartItemService;
import com.example.pizza_backend.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    @MockBean
    private CartItemService cartItemService;

    private CartDto mockCart;
    private CartItemDto mockCartItem;

    @BeforeEach
    void setup() {
        mockCartItem = CartItemDto.builder()
                .cartItemId(1L)
                .productId(101L)
                .productName("Pizza Margherita")
                .productDetail("Cheese Pizza")
                .productPrice(250)
                .qty(2)
                .lineTotal(500)
                .build();

        mockCart = CartDto.builder()
                .cartId(1L)
                .profileId(1001L)
                .username("testuser")
                .cartItems(List.of()) // ใน controller จะเรียก service อีกตัวดึง cartItems
                .build();
    }

    @Test
    void testGetCart() throws Exception {
        Mockito.when(cartService.getCartDtoByProfileId(anyLong())).thenReturn(mockCart);
        Mockito.when(cartItemService.getCartItemsByCartId(anyLong())).thenReturn(List.of(mockCartItem));

        mockMvc.perform(get("/cart/list")
                        .requestAttr("profile_id", 1001L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cart.cartId").value(mockCart.getCartId()))
                .andExpect(jsonPath("$.cartItems[0].cartItemId").value(mockCartItem.getCartItemId()));
    }

    @Test
    void testAddItem() throws Exception {
        CartItemInput input = new CartItemInput();
        input.setProductId(101L);
        input.setQty(2);

        Mockito.when(cartItemService.createCartItem(any(CartItemInput.class), anyLong()))
                .thenReturn("success");

        mockMvc.perform(post("/cart/addItems")
                        .requestAttr("profile_id", 1001L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Add items successfully"));
    }

    @Test
    void testUpdateItem() throws Exception {
        CartItemInput input = new CartItemInput();
        input.setCartItemId(1L);
        input.setQty(3);

        Mockito.when(cartItemService.updateCartItem(any(CartItemInput.class), anyLong()))
                .thenReturn("success");

        mockMvc.perform(post("/cart/updateItems")
                        .requestAttr("profile_id", 1001L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Update items successfully"));
    }

    @Test
    void testDeleteItem() throws Exception {
        CartItemInput input = new CartItemInput();
        input.setCartItemId(1L);

        Mockito.when(cartItemService.deleteCartItem(any(CartItemInput.class), anyLong()))
                .thenReturn("success");

        mockMvc.perform(post("/cart/deleteItems")
                        .requestAttr("profile_id", 1001L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Delete items successfully"));
    }
}
