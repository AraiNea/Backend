package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.CartDto;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.exception.IdNotFoundException;
import com.example.pizza_backend.persistence.entity.Cart;
import com.example.pizza_backend.persistence.repository.CartRepository;
import com.example.pizza_backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;
    private Mapper mapper;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, Mapper mapper) {
        this.cartRepository = cartRepository;
        this.mapper = mapper;
    }

    @Override
    public CartDto getCartDtoByProfileId(Long profileId) {
        Cart cart = cartRepository.findByProfileProfileId(profileId)
                .orElseThrow(() -> new IdNotFoundException("Cart not found for this user"));
        return mapper.toCartDto(cart);
    }
}
