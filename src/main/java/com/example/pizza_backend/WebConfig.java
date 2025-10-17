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
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final AdminInterceptor adminInterceptor;

    @Autowired
    public WebConfig(@NonNull AuthInterceptor authInterceptor, @NonNull AdminInterceptor adminInterceptor) {
        this.authInterceptor = authInterceptor;
        this.adminInterceptor = adminInterceptor;
    }

    // ✅ Interceptor
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/cart/**", "/order/**", "/address/**", "/profile/update", "/profile/list", "/profile/me")
                .excludePathPatterns("/login/**");

        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**", "/product/**", "/order/**", "/category/**", "/recommend/**")
                .excludePathPatterns("/product/list",
                        "/order/list","/order/create", "/order/",
                        "/category/list", "/category/");
    }

    // ✅ CORS (อย่าใส่ใน @Bean)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000","http://localhost:8888")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

