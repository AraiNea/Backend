package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.OrderItemDto;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.persistence.entity.OrderItem;
import com.example.pizza_backend.persistence.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrderItemServiceImplTest {

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private Mapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getOrderItems_shouldReturnOrderItemDtoList() {
        Long orderId = 1L;

        // ----- Mock Entity -----
        OrderItem item1 = new OrderItem();
        item1.setOrderItemId(1L);
        item1.setProductIdSnapshot(101L);
        item1.setProductName("Pizza");
        item1.setProductDetail("Cheese crust");
        item1.setProductPrice(250);    // Double
        item1.setQty(2);
        item1.setLineTotal(500);       // Double

        OrderItem item2 = new OrderItem();
        item2.setOrderItemId(2L);
        item2.setProductIdSnapshot(102L);
        item2.setProductName("Pasta");
        item2.setProductDetail("Carbonara");
        item2.setProductPrice(180);    // Double
        item2.setQty(1);
        item2.setLineTotal(180);       // Double

        // ----- Mock DTO -----
        OrderItemDto dto1 = OrderItemDto.builder()
                .orderItemId(item1.getOrderItemId())
                .productIdSnapshot(item1.getProductIdSnapshot())
                .productName(item1.getProductName())
                .productDetail(item1.getProductDetail())
                .productPrice(250.0)
                .qty(item1.getQty())
                .lineTotal(500)
                .build();

        OrderItemDto dto2 = OrderItemDto.builder()
                .orderItemId(item2.getOrderItemId())
                .productIdSnapshot(item2.getProductIdSnapshot())
                .productName(item2.getProductName())
                .productDetail(item2.getProductDetail())
                .productPrice(180.0)
                .qty(item2.getQty())
                .lineTotal(180)
                .build();

        // ----- Mock Repo + Mapper -----
        when(orderItemRepository.findByOrderOrderId(orderId))
                .thenReturn(List.of(item1, item2));

        when(mapper.toOrderItemDto(item1)).thenReturn(dto1);
        when(mapper.toOrderItemDto(item2)).thenReturn(dto2);

        // ----- Run -----
        List<OrderItemDto> result = orderItemService.getOrderItems(orderId);

        // ----- Validate -----
        assertThat(result)
                .hasSize(2)
                .containsExactly(dto1, dto2);

        verify(orderItemRepository).findByOrderOrderId(orderId);
        verify(mapper).toOrderItemDto(item1);
        verify(mapper).toOrderItemDto(item2);
    }

    @Test
    void getOrderItems_shouldReturnEmptyList_whenNoItems() {
        Long orderId = 99L;

        when(orderItemRepository.findByOrderOrderId(orderId))
                .thenReturn(List.of());

        List<OrderItemDto> result = orderItemService.getOrderItems(orderId);

        assertThat(result).isEmpty();
        verify(orderItemRepository).findByOrderOrderId(orderId);
        verifyNoInteractions(mapper);
    }
}
