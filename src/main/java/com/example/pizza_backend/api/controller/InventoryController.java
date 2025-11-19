package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.CategoryDto;
import com.example.pizza_backend.api.dto.InventoryDto;
import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.RecommendedProductDto;
import com.example.pizza_backend.api.dto.input.InventoryInput;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.service.InventoryService;
import com.example.pizza_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stock")
public class InventoryController {
    private final InventoryService inventoryService;
    private final ProductService productService;

    @Autowired
    public InventoryController(InventoryService inventoryService, ProductService productService) {
        this.inventoryService = inventoryService;
        this.productService = productService;
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getStockTransaction(@ModelAttribute InventoryInput req) {
        ProductSearchReq productSearchReq = new ProductSearchReq();
        productSearchReq.setProductName(req.getProductName());
        List<ProductDto> products = productService.getAllProducts(productSearchReq);
        if (products.isEmpty()) {
            Map<String, Object> total = new LinkedHashMap<>();
            total.put("remaining", 0);
            total.put("stockOut", 0);

            List<Object> emptyStockTable = new ArrayList<>();

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("TotalInventory", total);
            response.put("StockTable", emptyStockTable);  // ส่ง List ว่าง

            return ResponseEntity.ok(response);
        }

        Integer totalRemaining = inventoryService.getTotalInventory(products, 1, req);
        Integer totalOut = inventoryService.getTotalInventory(products, 2, req);

        List<InventoryDto> inventory = inventoryService.getInventory(products, req);

        Map<String, Object> total = new LinkedHashMap<>();
        total.put("remaining", totalRemaining);
        total.put("stockOut", totalOut);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("TotalInventory", total);
        response.put("StockTable", inventory);
        return ResponseEntity.ok(response);
    }
}
