package com.example.pizza_backend.Auth;


import com.example.pizza_backend.exception.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        try {
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                return true;
            }
            // 1. หา cookie ชื่อ tokenpizza
            Cookie[] cookies = request.getCookies();
            if (cookies == null || cookies.length == 0) {
                throw new UnauthorizedException("No cookies found");
            }

            String token = null;
            for (Cookie cookie : cookies) {
                if ("tokenpizza".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }

            if (token == null) {
                throw new UnauthorizedException("No tokenpizza found");
            }

            // 2. ถอด JWT ด้วย secret
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            Long profileId = claims.getBody().get("profile_id", Long.class);
            String username = claims.getBody().get("username", String.class);
            Long profileRole = claims.getBody().get("profile_role", Long.class);
            request.setAttribute("profile_id", profileId);
            request.setAttribute("username", username);
            request.setAttribute("profile_role", profileRole);

            // 3. ตรวจ role ว่าเป็น admin (user_role == 2)
            Integer role = claims.getBody().get("profile_role", Integer.class);
            if (role != null && role == 2) {
                return true; // ✅ ผ่าน
            }


            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not admin");
            return false;

        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return false;
        }
    }
}
