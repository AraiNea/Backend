package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.RecommendedProductDto;
import com.example.pizza_backend.api.dto.input.ProductInput;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.service.ProductService;
import com.example.pizza_backend.service.RecommendedService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final RecommendedService recommendedService;
    @Autowired
    public ProductController(ProductService productService, RecommendedService recommendedService) {
        this.productService = productService;
        this.recommendedService = recommendedService;
    }


    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllProducts(@ModelAttribute ProductSearchReq productSearchReq) {
        List<ProductDto> products = productService.getAllProducts(productSearchReq);
        List<RecommendedProductDto> rec = recommendedService.getAllRecommendedProducts();
        List<Long> productIds = rec.stream()
                .map(RecommendedProductDto::getProductId)
                .toList();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("products", products);
        response.put("recommendProductId", productIds);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(
                                            HttpServletRequest request,
                                           @RequestPart("product") ProductInput productInput,
                                           @RequestPart("image") MultipartFile imageFile) throws IOException {
        String username = (String) request.getAttribute("username");
        String createLog = productService.createProduct(productInput, imageFile, username);
        if (createLog == "success") {
            return  ResponseEntity.ok()
                    .body(Map.of("message", "create success"));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProduct(
            HttpServletRequest request,
            @RequestPart("product") ProductInput productInput,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {
        String username = (String) request.getAttribute("username");
        String createLog="";
        if (imageFile != null && !imageFile.isEmpty()) {
            // ถ้ามีการส่งไฟล์มา, ให้ update ไฟล์ภาพ
            createLog = productService.updateProduct(productInput, imageFile, username);
        } else {
            // ถ้าไม่มีไฟล์ภาพ, ให้ทำการ update โดยไม่มีการเปลี่ยนแปลงไฟล์
            createLog = productService.updateProduct(productInput, null, username);
        }

        if (createLog == "success") {
            return  ResponseEntity.ok()
                    .body(Map.of("message", "update success"));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteProduct(
            @RequestBody ProductInput productInput) throws IOException {
        System.out.println(productInput);
        String createLog= productService.deleteProduct(productInput);
        if (createLog == "success") {
            return  ResponseEntity.ok()
                    .body(Map.of("message", "delete success"));
        }
        return ResponseEntity.badRequest().build();
    }

}
