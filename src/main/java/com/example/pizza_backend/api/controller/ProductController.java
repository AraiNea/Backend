package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.input.ProductInput;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/list")
    public ResponseEntity<List<ProductDto>> getAllProducts(@ModelAttribute ProductSearchReq productSearchReq) {
        List<ProductDto> products = productService.getAllProducts(productSearchReq);
        return ResponseEntity.ok(products);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestPart("product") ProductInput productInput,
                                                    @RequestPart("image") MultipartFile imageFile) {
        String createLog = productService.createProduct(productInput, imageFile);
        if (createLog == "success") {
            return  ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
