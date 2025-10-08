package com.example.pizza_backend;

import com.example.pizza_backend.Auth.AdminInterceptor;
import com.example.pizza_backend.Auth.AuthInterceptor;
import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    private final AuthInterceptor authInterceptor;
    private final AdminInterceptor adminInterceptor;

    @Autowired
    public WebConfig(@NonNull AuthInterceptor authInterceptor, @NonNull AdminInterceptor adminInterceptor) {
        this.authInterceptor = authInterceptor;
        this.adminInterceptor = adminInterceptor;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            // ✅ Interceptor สำหรับตรวจ JWT
            @Override
            public void addInterceptors(@NonNull InterceptorRegistry registry) {
                registry.addInterceptor(authInterceptor)
                        .addPathPatterns("/cart/**","/order/**","/address/**","/profile/update","/profile/list")         // ตรวจเฉพาะ path นี้
                        .excludePathPatterns("/login/**");    // ยกเว้น path นี้
                registry.addInterceptor(adminInterceptor)
                        .addPathPatterns("/admin/**","/product/**"
//                                ,"/product/create","product/update","product/delete"
                        )
                        .excludePathPatterns("/product/list");
            }

            // ✅ CORS สำหรับอนุญาต cross-origin (เช่น React ที่ 5173)
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true); // ถ้าจะส่ง cookie ไปกลับ
            }
        };
    }
}
