package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    @Autowired
    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductDto>> getAllProducts(@ModelAttribute ProductSearchReq productSearchReq) {
        List<ProductDto> products = productService.getAllProducts(productSearchReq);
        return ResponseEntity.ok(products);
    }
}
