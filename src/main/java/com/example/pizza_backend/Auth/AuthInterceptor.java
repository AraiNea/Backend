package com.example.pizza_backend.Auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        try {
            // 1. ดึง cookie ทั้งหมด
            System.out.println("AuthInterceptor preHandle");
            Cookie[] cookies = request.getCookies();
            if (cookies == null || cookies.length == 0) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: No cookies found");
                return false;
            }

            // 2. หา cookie ที่ชื่อว่า "tokenpizza"
            String token = null;
            for (Cookie cookie : cookies) {
                System.out.println("🍪 Cookie: " + cookie.getName() + " = " + cookie.getValue());
                if ("tokenpizza".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }

            if (token == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: No token found");
                return false;
            }

            // 3. เตรียม key และถอด token
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); // ถ้าผิดจะโยน exception ออกมา

            // 4. ถ้าอยากดึงค่า user จาก token:
            Long profileId = claims.getBody().get("profile_id", Long.class);
            String profileName = claims.getBody().get("profile_name", String.class);
            Long profileRole = claims.getBody().get("profile_role", Long.class);
            request.setAttribute("profile_id", profileId);
            request.setAttribute("profile_name", profileName);
            request.setAttribute("profile_role", profileRole);

            // 5. ✅ ผ่านการตรวจสอบแล้ว → ปล่อยผ่าน
            return true;

        } catch (JwtException e) {
            // ถอด token ไม่ได้, ลายเซ็นไม่ตรง, หมดอายุ ฯลฯ
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid token");
            return false;
        }
    }

}
