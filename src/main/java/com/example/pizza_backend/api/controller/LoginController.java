package com.example.pizza_backend.api.controller;


import com.example.pizza_backend.api.dto.input.LoginInput;
import com.example.pizza_backend.api.dto.input.ProfileInput;
import com.example.pizza_backend.persistence.entity.Profile;
import com.example.pizza_backend.persistence.repository.ProfileRepository;
import com.example.pizza_backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final ProfileRepository profileRepository;
    private final JwtService jwtService;

    @Autowired
    public LoginController(ProfileRepository profileRepository, JwtService jwtService) {
        this.profileRepository = profileRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody LoginInput req) {
        Optional<Profile> userOpt = profileRepository
                .findFirstByUsernameAndPassword(req.getUsername(), req.getPassword());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "unauthorized"));
        }

        Profile user = userOpt.get();

        // ✅ เตรียม payload
        Map<String, Object> payload = Map.of(
                "profile_id", user.getProfileId(),
                "user_name", user.getProfileName(),
                "user_role", user.getProfileRole()
        );

        // ✅ สร้าง token
        String token = jwtService.generateToken(payload);

        // ✅ ใส่ cookie
        ResponseCookie cookie = ResponseCookie.from("tokenpizza", token)
                .httpOnly(true)
                .secure(false) // เปลี่ยนเป็น true ตอน production
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(30))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "success"));
    }

    @PostMapping("/user/signup")
    public ResponseEntity<?> signUp(@RequestBody ProfileInput req) {
        // 1. ตรวจสอบว่า username ซ้ำไหม
        if (profileRepository.existsByUsername(req.getUsername())) {
            return ResponseEntity.status(409).body(Map.of("message", "username already taken"));
        }

        // 2. สร้างและบันทึก user ใหม่
        Profile user = new Profile();
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setProfileName(req.getProfileName());
        user.setProfileRole(1); // สมัครใหม่ = user ปกติ
        profileRepository.save(user);
        System.out.println(user.getProfileId());

        // 3. สร้าง JWT
        String token = jwtService.generateToken(Map.of(
                "user_id", user.getProfileId(),
                "user_name", user.getProfileName(),
                "user_role", user.getProfileRole()
        ));

        // 4. ส่งกลับใน cookie
        ResponseCookie cookie = ResponseCookie.from("tokenpizza", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofDays(30))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "signup success"));
    }

}
