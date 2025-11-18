package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.CartDto;
import com.example.pizza_backend.api.dto.CartItemDto;
import com.example.pizza_backend.api.dto.DiscountCodeDto;
import com.example.pizza_backend.api.dto.search.DiscountCodeSearchReq;
import com.example.pizza_backend.service.DiscountCodeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/code")
public class DiscountController {

    private DiscountCodeService discountCodeService;

    @Autowired
    public DiscountController(DiscountCodeService discountCodeService) {
        this.discountCodeService = discountCodeService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getCode(DiscountCodeSearchReq request) {

        DiscountCodeDto code = discountCodeService.getDiscountCode(request);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("results", code);

        return ResponseEntity.ok(response);
    }
}
