package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.RecommendedProductDto;
import com.example.pizza_backend.api.dto.input.ProductInput;
import com.example.pizza_backend.api.dto.input.RecommendedInput;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.service.RecommendedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recommend")
public class RecommendedController {

    private final RecommendedService recommendedService;

    @Autowired
    public RecommendedController(RecommendedService recommendedService) {
        this.recommendedService = recommendedService;
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllRecommended() {
        List<RecommendedProductDto> rec = recommendedService.getAllRecommendedProducts();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("recommended", rec);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRecommended(
//                                            HttpServletRequest request,
            @RequestPart("recommended") RecommendedInput recommendedInput,
            @RequestPart("image") MultipartFile imageFile) throws IOException {
//        String usersame = (String) request.getAttribute("username");
        String username="temp";
        String createLog = recommendedService.createRecommended(recommendedInput, imageFile);
        if (createLog == "success") {
            return  ResponseEntity.ok()
                    .body(Map.of("message", "create success"));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateRecommended(
//                                            HttpServletRequest request,
            @RequestPart("recommended") RecommendedInput recommendedInput,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {
//        String usersame = (String) request.getAttribute("username");
        String username="temp";
        String createLog="";
        if (imageFile != null && !imageFile.isEmpty()) {
            // ถ้ามีการส่งไฟล์มา, ให้ update ไฟล์ภาพ
            createLog = recommendedService.updateRecommended(recommendedInput, imageFile);
        } else {
            // ถ้าไม่มีไฟล์ภาพ, ให้ทำการ update โดยไม่มีการเปลี่ยนแปลงไฟล์
            createLog = recommendedService.updateRecommended(recommendedInput, null);
        }

        if (createLog == "success") {
            return  ResponseEntity.ok()
                    .body(Map.of("message", "update success"));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteRecommended(
//          HttpServletRequest request,
            @RequestBody RecommendedInput recommendedInput) throws IOException {
        System.out.println(recommendedInput);
        String createLog= recommendedService.deleteRecommended(recommendedInput);
        if (createLog == "success") {
            return  ResponseEntity.ok()
                    .body(Map.of("message", "delete success"));
        }
        return ResponseEntity.badRequest().build();
    }
}
