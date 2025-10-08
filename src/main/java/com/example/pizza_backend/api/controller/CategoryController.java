package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.CategoryDto;
import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.input.CategoryInput;
import com.example.pizza_backend.api.dto.search.CategorySearchReq;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.service.CategoryService;
import com.example.pizza_backend.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private ProductService productService;
    private CategoryService categoryService;

    @Autowired
    public void setProductService(ProductService productService,  CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllCategories(@ModelAttribute ProductSearchReq req) {
        List<CategoryDto> categories = categoryService.getAllCategories();
        List<ProductDto> products = productService.getAllProducts(req);


        Map<String, Object> response = new LinkedHashMap<>();
        response.put("categories", categories);
        response.put("products", products);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllCategoriesOnly(@ModelAttribute CategorySearchReq req) {
        List<CategoryDto> categories = categoryService.getAllCategories(req);


        Map<String, Object> response = new LinkedHashMap<>();
        response.put("categories", categories);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/create")
    public ResponseEntity<?> createProduct(
            HttpServletRequest request,
            @RequestPart("category") CategoryInput categoryInput,
            @RequestPart("image") MultipartFile imageFile) throws IOException {
        String username = (String) request.getAttribute("username");
        String createLog = categoryService.createCategory(categoryInput, imageFile, username);
        if (createLog == "success") {
            return  ResponseEntity.ok()
                    .body(Map.of("message", "create success"));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProduct(
            HttpServletRequest request,
            @RequestPart(value = "category") CategoryInput categoryInput,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {
        String username = (String) request.getAttribute("username");
        String createLog="";
        if (imageFile != null && !imageFile.isEmpty()) {
            // ถ้ามีการส่งไฟล์มา, ให้ update ไฟล์ภาพ
            createLog = categoryService.updateCategory(categoryInput, imageFile, username);
        } else {
            // ถ้าไม่มีไฟล์ภาพ, ให้ทำการ update โดยไม่มีการเปลี่ยนแปลงไฟล์
            createLog = categoryService.updateCategory(categoryInput, null, username);
        }

        if (createLog == "success") {
            return  ResponseEntity.ok()
                    .body(Map.of("message", "update success"));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteProduct(
            @RequestBody CategoryInput categoryInput) throws IOException {
        String createLog= categoryService.deleteCategory(categoryInput);
        if (createLog == "success") {
            return  ResponseEntity.ok()
                    .body(Map.of("message", "delete success"));
        }
        return ResponseEntity.badRequest().build();
    }
}
