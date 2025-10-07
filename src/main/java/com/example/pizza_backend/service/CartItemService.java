package com.example.pizza_backend.service;

import com.example.pizza_backend.api.dto.CartItemDto;
import com.example.pizza_backend.api.dto.input.CartItemInput;

import java.util.List;

public interface CartItemService {
    List<CartItemDto> getCartItemsByCartId(Long cartId);
    String createCartItem(CartItemInput cartItemInput, Long profileId);
    String updateCartItem(CartItemInput cartItemInput, Long profileId);
    String deleteCartItem(CartItemInput cartItemInput, Long profileId);
    String clearAllCartItem(Long profileId);
}
