package com.example.pizza_backend.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;
    private final String secret = "MySuperSecretKeyForJWTTesting1234567890!";
    private final int expiresDays = 1;

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();

        // inject private jwtSecret
        Field secretField = JwtService.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        secretField.set(jwtService, secret);

        // inject private expiresDays
        Field expiresField = JwtService.class.getDeclaredField("expiresDays");
        expiresField.setAccessible(true);
        expiresField.set(jwtService, expiresDays);
    }

    @Test
    void testGenerateToken() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("profile_id", 1L);
        payload.put("username", "daraporn");
        payload.put("profile_role", 2);

        String token = jwtService.generateToken(payload);

        assertThat(token).isNotNull().isNotEmpty();

        // decode token
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.get("profile_id", Long.class)).isEqualTo(1L);
        assertThat(claims.get("username", String.class)).isEqualTo("daraporn");
        assertThat(claims.get("profile_role", Integer.class)).isEqualTo(2);

        assertThat(claims.getExpiration()).isNotNull();
        assertThat(claims.getIssuedAt()).isNotNull();
    }
}
