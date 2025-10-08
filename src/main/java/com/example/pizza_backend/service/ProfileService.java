package com.example.pizza_backend.service;

import com.example.pizza_backend.api.dto.input.LoginInput;
import com.example.pizza_backend.api.dto.input.ProfileInput;
import com.example.pizza_backend.persistence.entity.Profile;

import java.util.Optional;

public interface ProfileService {
    Boolean checkDuplicateProfile(ProfileInput req);
    Optional<Profile> checkLogIn(LoginInput req);
    String createProfileWithAddress(ProfileInput req, Integer role);
    String updateProfile(ProfileInput req, Long profileId);
}
