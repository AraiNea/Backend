package com.example.pizza_backend.service;

import com.example.pizza_backend.api.dto.InventoryDto;
import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.input.InventoryInput;

import java.util.List;

public interface InventoryService {
    Integer getTotalInventory(List<ProductDto> products, Integer type, InventoryInput req);
    List<InventoryDto> getInventory(List<ProductDto> products, InventoryInput req);
}
