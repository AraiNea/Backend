package com.example.pizza_backend.api.controller;


import com.example.pizza_backend.api.dto.CategoryDto;
import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.service.CategoryService;
import com.example.pizza_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/home")
public class HomeController {

    private ProductService productService;
    private CategoryService categoryService;

    @Autowired
    public void setProductService(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getHomeInfo(@ModelAttribute ProductSearchReq req) {
        List<CategoryDto> categories = categoryService.getAllCategories();
        List<ProductDto> products = productService.getAllProducts(req);
        //จัดกลุ่ม product ตาม categoryId
        Map<Long, List<ProductDto>> productsByCategory = products.stream()
                .collect(Collectors.groupingBy(
                p -> p.getCategoryId()
                ));
        //เอา product ที่จัดกลุ่มไว้ ยัดใส่แต่ละ category
        List<Map<String, Object>> results = categories.stream()
                .map(c -> Map.of(
                        "category", c,
                        "products", productsByCategory.getOrDefault(c.getCategoryId(), List.of())
                ))
                .toList();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("results", results);

        return ResponseEntity.ok(response);
    }

}
