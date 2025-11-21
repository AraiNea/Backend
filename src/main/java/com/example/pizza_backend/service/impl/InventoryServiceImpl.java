package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.InventoryDto;
import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.input.InventoryInput;
import com.example.pizza_backend.persistence.repository.InventoryRepository;
import com.example.pizza_backend.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Integer getTotalInventory(List<ProductDto> products, Integer type, InventoryInput req) {
        Integer totalInventory = 0;

        // กำหนดค่า default สำหรับ start และ end
        LocalDateTime start = LocalDateTime.now();
        if (req.getStartTime() == null) {
            // ถ้า startTime เป็น null, ใช้วันที่เริ่มต้นที่ต้องการ
            start = LocalDateTime.parse("2024-01-01T00:00:00");
        } else {
            start = LocalDateTime.of(req.getStartTime(), LocalTime.MIN);
        }

        LocalDateTime end = LocalDateTime.now(); // กำหนดค่า default สำหรับ end เป็นวันนี้
        if (req.getEndTime() != null) {
            end = LocalDateTime.of(req.getEndTime(), LocalTime.MAX);
        }

        if (type == 1) {
            for (ProductDto product : products) {
                Integer stock = inventoryRepository.sumQtyChangeByProductIdAndType(product.getProductId(), 1, start, end);
                Integer out = inventoryRepository.sumQtyChangeByProductIdAndType(product.getProductId(), 3, start, end);

                stock = (stock != null) ? stock : 0;
                out = (out != null) ? out : 0;
                Integer temp = stock - out;
                totalInventory += (temp != null ? temp : 0);
            }
        }

        if (type == 2) {
            for (ProductDto product : products) {
                Integer sold = inventoryRepository.sumQtyChangeByProductIdAndType(product.getProductId(), 2, start, end);

                sold = (sold != null) ? sold : 0;
                totalInventory += (sold != null ? sold : 0);
            }
        }
        return totalInventory;
    }

    @Override
    public List<InventoryDto> getInventory(List<ProductDto> products, InventoryInput req) {

        // กำหนดค่า default สำหรับ start และ end
        LocalDateTime start = LocalDateTime.now();
        if (req.getStartTime() == null) {
            // ถ้า startTime เป็น null, ใช้วันที่เริ่มต้นที่ต้องการ
            start = LocalDateTime.parse("2024-01-01T00:00:00");
        } else {
            start = LocalDateTime.of(req.getStartTime(), LocalTime.MIN);
        }

        LocalDateTime end = LocalDateTime.now(); // กำหนดค่า default สำหรับ end เป็นวันนี้
        if (req.getEndTime() != null) {
            end = LocalDateTime.of(req.getEndTime(), LocalTime.MAX);
        }

        List<InventoryDto> inventoryDtoList = new ArrayList<>();

        // วนลูปผ่านแต่ละ product ที่เสิร์ชเจอ
        for (ProductDto product : products) {
            Integer stock = inventoryRepository.sumQtyChangeByProductIdAndType(product.getProductId(), 1, start, end);
            Integer sold = inventoryRepository.sumQtyChangeByProductIdAndType(product.getProductId(), 2, start, end);
            Integer out = inventoryRepository.sumQtyChangeByProductIdAndType(product.getProductId(), 3, start, end);

            stock = (stock != null) ? stock : 0;
            sold = (sold != null) ? sold : 0;
            out = (out != null) ? out : 0;
            Integer temp = stock - sold - out;

            InventoryDto inventory = InventoryDto.builder()
                    .productName(product.getProductName())
                    .productId(product.getProductId())
                    .stock(temp)
                    .sold(sold)
                    .categoryName(product.getCategoryName())
                    .price(product.getProductPrice())
                    .build();
            inventoryDtoList.add(inventory);
        }

        return inventoryDtoList;
    }
}
