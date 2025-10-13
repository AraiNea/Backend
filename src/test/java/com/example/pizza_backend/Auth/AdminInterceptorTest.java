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

class AdminInterceptorTest {

    private AdminInterceptor interceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private final String secret = "MySuperSecretKeyForJWTTesting1234567890!";

    @BeforeEach
    void setUp() throws Exception {
        interceptor = new AdminInterceptor();

        // ใช้ Reflection inject private jwtSecret
        Field field = AdminInterceptor.class.getDeclaredField("jwtSecret");
        field.setAccessible(true);
        field.set(interceptor, secret);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    // helper: สร้าง JWT
    private String generateToken(Long profileId, String role) {
        return Jwts.builder()
                .claim("profile_role", role.equals("admin") ? 2 : 1)
                .claim("profile_id", profileId)
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    @Test
    void testNoCookies() throws Exception {
        when(request.getCookies()).thenReturn(null);

        boolean result = interceptor.preHandle(request, response, new Object());
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "No cookies found");
        assertThat(result).isFalse();
    }

    @Test
    void testNoTokenCookie() throws Exception {
        Cookie[] cookies = {new Cookie("other", "value")};
        when(request.getCookies()).thenReturn(cookies);

        boolean result = interceptor.preHandle(request, response, new Object());
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token found");
        assertThat(result).isFalse();
    }

    @Test
    void testInvalidToken() throws Exception {
        Cookie[] cookies = {new Cookie("tokenpizza", "invalidToken")};
        when(request.getCookies()).thenReturn(cookies);

        boolean result = interceptor.preHandle(request, response, new Object());
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        assertThat(result).isFalse();
    }

    @Test
    void testValidAdminToken() throws Exception {
        String token = generateToken(1L, "admin");
        Cookie[] cookies = {new Cookie("tokenpizza", token)};
        when(request.getCookies()).thenReturn(cookies);

        boolean result = interceptor.preHandle(request, response, new Object());
        assertThat(result).isTrue();
    }

    @Test
    void testNonAdminToken() throws Exception {
        String token = generateToken(1L, "user");
        Cookie[] cookies = {new Cookie("tokenpizza", token)};
        when(request.getCookies()).thenReturn(cookies);

        boolean result = interceptor.preHandle(request, response, new Object());
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not admin");
        assertThat(result).isFalse();
    }
}
