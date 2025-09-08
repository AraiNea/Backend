package com.example.pizza_backend.service;

import com.example.pizza_backend.api.dto.CartDto;

public interface CartService {
    CartDto getCartDtoByProfileId(Long profileId);
}
