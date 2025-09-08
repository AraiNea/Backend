package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.input.LoginInputReq;
import com.example.pizza_backend.api.dto.input.ProfileInputReq;
import com.example.pizza_backend.api.mapper.ProfileMapper;
import com.example.pizza_backend.persistence.entity.Address;
import com.example.pizza_backend.persistence.entity.Profile;
import com.example.pizza_backend.persistence.repository.AddressRepository;
import com.example.pizza_backend.persistence.repository.ProfileRepository;
import com.example.pizza_backend.service.JwtService;
import com.example.pizza_backend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final JwtService jwtService;
    private final ProfileMapper profileMapper;
    private final AddressRepository addressRepository;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository, JwtService jwtService, ProfileMapper profileMapper, AddressRepository addressRepository) {
        this.profileRepository = profileRepository;
        this.jwtService = jwtService;
        this.profileMapper = profileMapper;
        this.addressRepository = addressRepository;
    }


    @Override
    public Optional<Profile> checkLogIn(LoginInputReq req) {
        Optional<Profile> userOpt = profileRepository
                .findFirstByUsernameAndPassword(req.getUsername(), req.getPassword());
        return userOpt;
    }

    @Override
    public Boolean checkDuplicateProfile(ProfileInputReq req) {
        if (profileRepository.existsByUsername(req.getUsername())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String createProfileWithAddress(ProfileInputReq req, Integer role) {
        // 2. สร้างและบันทึก user ใหม่
//        Profile user = Profile.builder()
//                .username(req.getUsername())
//                .password(req.getPassword())
//                .profileName(req.getProfileName())
//                .profileSname(req.getProfileSname())
//                .createdAt(LocalDate.now())
//                .profileRole(role)
//                .build();
//
//
//        Address address = new Address();
//        address.setPhone(req.getPhone());
//        address.setAmphor(req.getAmphor());
//        address.setDistrict(req.getDistrict());
//        address.setProvince(req.getProvince());
//        address.setZipCode(req.getZipCode());
//        address.setAddrNum(req.getAddrNum());
//        address.setDetail(req.getDetail());
//        address.setReceivedName(req.getReceivedName());
        Profile user = profileMapper.toProfile(req, role);
        Address address = profileMapper.toAddress(req);

        user.setAddress(address);
        address.setProfile(user);

        profileRepository.save(user);
        addressRepository.save(address);

        //สร้าง JWT แล้วส่ง token กลับไป
        String token = jwtService.generateToken(Map.of(
                "user_id", user.getProfileId(),
                "user_name", user.getProfileName(),
                "user_role", user.getProfileRole()
        ));
        return token;
    }
}
