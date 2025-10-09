package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.PizzaBackendApplication;
import com.example.pizza_backend.persistence.entity.Category;
import com.example.pizza_backend.persistence.entity.Product;
import com.example.pizza_backend.persistence.repository.CategoryRepository;
import com.example.pizza_backend.persistence.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = PizzaBackendApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test") // ใช้ application-test.properties
class HomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // ลบข้อมูลเก่า
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        // สร้าง category และ product ตัวอย่าง
        Category pizza = categoryRepository.save(Category.builder().categoryName("Pizza").build());
        Category drinks = categoryRepository.save(Category.builder().categoryName("Drinks").build());

        productRepository.save(Product.builder()
                .productName("Margherita")
                .category(pizza)
                .productPrice(200)
                .build());

        productRepository.save(Product.builder()
                .productName("Coke")
                .category(drinks)
                .productPrice(50)
                .build());
    }

    @Test
    void testGetHomeInfo_integration() throws Exception {
        mockMvc.perform(get("/home/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].category.categoryName").value("Pizza"))
                .andExpect(jsonPath("$.results[0].products[0].productName").value("Margherita"))
                .andExpect(jsonPath("$.results[1].category.categoryName").value("Drinks"))
                .andExpect(jsonPath("$.results[1].products[0].productName").value("Coke"));
    }
}

