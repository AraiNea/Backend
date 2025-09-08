package com.example.pizza_backend.service;

import com.example.pizza_backend.api.dto.input.LoginInputReq;
import com.example.pizza_backend.api.dto.input.ProfileInputReq;
import com.example.pizza_backend.persistence.entity.Profile;

import java.util.Optional;

public interface ProfileService {
    Boolean checkDuplicateProfile(ProfileInputReq req);
    Optional<Profile> checkLogIn(LoginInputReq req);
    String createProfileWithAddress(ProfileInputReq req, Integer role);
}
