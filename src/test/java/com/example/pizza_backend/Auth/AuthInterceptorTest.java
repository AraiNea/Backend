package com.example.pizza_backend.Auth;

import com.example.pizza_backend.exception.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthInterceptorTest {

    private AuthInterceptor interceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private final String secret = "MySuperSecretKeyForJWTTesting1234567890!";

    @BeforeEach
    void setUp() throws Exception {
        interceptor = new AuthInterceptor();

        // ใช้ Reflection inject ค่า private field jwtSecret
        Field field = AuthInterceptor.class.getDeclaredField("jwtSecret");
        field.setAccessible(true);
        field.set(interceptor, secret);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    // Helper: generate JWT token
    private String generateToken(Long profileId, String username, Long role) {
        return Jwts.builder()
                .claim("profile_id", profileId)
                .claim("username", username)
                .claim("profile_role", role)
                .setSubject("user")
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    @Test
    void testNoCookies() {
        when(request.getCookies()).thenReturn(null);

        UnauthorizedException ex = assertThrows(UnauthorizedException.class,
                () -> interceptor.preHandle(request, response, new Object()));
        assertThat(ex.getMessage()).isEqualTo("No cookies found");
    }

    @Test
    void testNoTokenCookie() {
        Cookie[] cookies = {new Cookie("other", "value")};
        when(request.getCookies()).thenReturn(cookies);

        UnauthorizedException ex = assertThrows(UnauthorizedException.class,
                () -> interceptor.preHandle(request, response, new Object()));
        assertThat(ex.getMessage()).isEqualTo("No tokenpizza found");
    }

    @Test
    void testInvalidToken() {
        Cookie[] cookies = {new Cookie("tokenpizza", "invalidToken")};
        when(request.getCookies()).thenReturn(cookies);

        UnauthorizedException ex = assertThrows(UnauthorizedException.class,
                () -> interceptor.preHandle(request, response, new Object()));
        assertThat(ex.getMessage()).isEqualTo("Invalid Token");
    }

    @Test
    void testValidToken() throws Exception {
        String token = generateToken(1L, "daraporn", 2L);
        Cookie[] cookies = {new Cookie("tokenpizza", token)};
        when(request.getCookies()).thenReturn(cookies);

        boolean result = interceptor.preHandle(request, response, new Object());

        assertThat(result).isTrue();
        verify(request).setAttribute("profile_id", 1L);
        verify(request).setAttribute("username", "daraporn");
        verify(request).setAttribute("profile_role", 2L);
    }
}
