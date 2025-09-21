package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.input.LoginInput;
import com.example.pizza_backend.api.dto.input.ProfileInput;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.persistence.entity.Address;
import com.example.pizza_backend.persistence.entity.Cart;
import com.example.pizza_backend.persistence.entity.Profile;
import com.example.pizza_backend.persistence.repository.AddressRepository;
import com.example.pizza_backend.persistence.repository.CartRepository;
import com.example.pizza_backend.persistence.repository.ProfileRepository;
import com.example.pizza_backend.service.JwtService;
import com.example.pizza_backend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final JwtService jwtService;
    private final Mapper mapper;
    private final ProfileRepository profileRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;

    @Autowired
    public ProfileServiceImpl(
            ProfileRepository profileRepository,
            JwtService jwtService,
            Mapper mapper,
            AddressRepository addressRepository,
            CartRepository cartRepository) {
        this.profileRepository = profileRepository;
        this.jwtService = jwtService;
        this.mapper = mapper;
        this.addressRepository = addressRepository;
        this.cartRepository = cartRepository;
    }


    @Override
    public Optional<Profile> checkLogIn(LoginInput req) {
        Optional<Profile> userOpt = profileRepository
                .findFirstByUsernameAndPassword(req.getUsername(), req.getPassword());
        return userOpt;
    }

    @Override
    public Boolean checkDuplicateProfile(ProfileInput req) {
        if (profileRepository.existsByUsername(req.getUsername())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String createProfileWithAddress(ProfileInput req, Integer role) {
        Profile user = mapper.toProfile(req, role);
        Address address = mapper.toAddress(req);

        //role=2=admin admin ไม่มี cart และ address
        if (role == 1) {
            user.setAddress(address);
            address.setProfile(user);
            Cart cart = new Cart();
            cart.setProfile(user);
            cart.setCreatedAt(LocalDate.now());
            cartRepository.save(cart);
            addressRepository.save(address);
        }
        user.setCreatedAt(LocalDate.now());
        user.setProfileRole(role);
        profileRepository.save(user);

        //สร้าง JWT แล้วส่ง token กลับไป
        String token = jwtService.generateToken(Map.of(
                "profile_id", user.getProfileId(),
                "username", user.getProfileName(),
                "profile_role", user.getProfileRole()
        ));
        return token;
    }
}
