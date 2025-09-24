package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.input.ProductInput;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllProducts(@ModelAttribute ProductSearchReq productSearchReq) {
        List<ProductDto> products = productService.getAllProducts(productSearchReq);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("products", products);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestPart("product") ProductInput productInput,
                                           @RequestPart("image") MultipartFile imageFile) throws IOException {
        String createLog = productService.createProduct(productInput, imageFile);
        if (createLog == "success") {
            return  ResponseEntity.ok()
                    .body(Map.of("message", "create success"));
        }
        return ResponseEntity.badRequest().build();
    }

}
