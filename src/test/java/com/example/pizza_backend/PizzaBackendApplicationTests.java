package com.example.pizza_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")   // <- บังคับใช้ H2
class PizzaBackendApplicationTests {

    @Test
    void contextLoads() { }
}
