package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.CartDto;
import com.example.pizza_backend.persistence.entity.Cart;
import com.example.pizza_backend.persistence.repository.CartRepository;
import com.example.pizza_backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
    @Override
    public CartDto getCartDtoByProfileId(Long profileId) {
        Cart cart = cartRepository.findByProfileProfileId(profileId);
        return CartDto.builder()
                .cartId(cart.getCartId())
                .username(cart.getProfile().getUsername())
                .build();
    }
}
