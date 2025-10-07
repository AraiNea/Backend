package com.example.pizza_backend.service;

import com.example.pizza_backend.api.dto.AddressDto;
import com.example.pizza_backend.api.dto.input.AddressInput;

public interface AddressService {
    AddressDto getAddressByProfileId(Long profileId);
    String updateAddress(AddressInput addressInput);
}
