package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.AddressDto;
import com.example.pizza_backend.api.dto.input.AddressInput;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.exception.IdNotFoundException;
import com.example.pizza_backend.persistence.entity.Address;
import com.example.pizza_backend.persistence.entity.Profile;
import com.example.pizza_backend.persistence.repository.AddressRepository;
import com.example.pizza_backend.persistence.repository.ProfileRepository;
import com.example.pizza_backend.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ProfileRepository profileRepository;
    private final Mapper mapper;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, ProfileRepository profileRepository, Mapper mapper) {
        this.addressRepository = addressRepository;
        this.profileRepository = profileRepository;
        this.mapper = mapper;
    }

    @Override
    public AddressDto getAddressByProfileId(Long profileId) {
        if (profileId == null) {
            throw new IllegalArgumentException("The given profile Id cannot be null");
        }
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IdNotFoundException("Profile Not Found"));
        Address address = addressRepository.findAddressByProfile(profile)
                .orElseThrow(() -> new IdNotFoundException("Address Not Found For This Profile"));
        return mapper.toAddressDto(address);
    }

    @Override
    public String updateAddress(AddressInput addressInput) {
        if (addressInput.getAddressId() == null) {
            throw new IllegalArgumentException("The given address Id cannot be null");
        }
        Address address = addressRepository.findById(addressInput.getAddressId())
                .orElseThrow(()-> new IdNotFoundException("Address Not Found"));
        mapper.updateAddressFromInput(addressInput, address);
        addressRepository.save(address);
        return "success";
    }
}
