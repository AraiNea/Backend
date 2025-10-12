package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.OrderDto;
import com.example.pizza_backend.api.dto.OrderItemDto;
import com.example.pizza_backend.api.dto.input.OrderAndItemInput;
import com.example.pizza_backend.api.dto.input.OrderInput;
import com.example.pizza_backend.api.dto.search.OrderSearchReq;
import com.example.pizza_backend.service.CartItemService;
import com.example.pizza_backend.service.OrderItemService;
import com.example.pizza_backend.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderItemService orderItemService;

    @MockBean
    private CartItemService cartItemService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderDto sampleOrder;
    private OrderItemDto sampleOrderItem;

    @BeforeEach
    void setUp() {
        sampleOrder = OrderDto.builder()
                .orderId(1L)
                .username("tester")
                .status(0)
                .subtotal(500)
                .deliveryFee(50)
                .grandTotal(550)
                .createdAt(LocalDate.now())
                .build();

        sampleOrderItem = OrderItemDto.builder()
                .orderItemId(1L)
                .productIdSnapshot(1L)
                .productName("Pizza Margherita")
                .productDetail("Cheese & Tomato")
                .productPrice(200)
                .qty(2)
                .lineTotal(400)
                .build();
    }

    @Test
    void testGetOrders() throws Exception {
        when(orderService.getOrdersByProfileId(1L)).thenReturn(List.of(sampleOrder));
        when(orderItemService.getOrderItems(1L)).thenReturn(List.of(sampleOrderItem));

        mockMvc.perform(get("/order/")
                        .requestAttr("profile_id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].order.orderId").value(1))
                .andExpect(jsonPath("$.results[0].order.username").value("tester"))
                .andExpect(jsonPath("$.results[0].orderItems[0].productName").value("Pizza Margherita"));
    }

    @Test
    void testGetAllOrders() throws Exception {
        when(orderService.getAllOrders(any(OrderSearchReq.class))).thenReturn(List.of(sampleOrder));
        when(orderItemService.getOrderItems(1L)).thenReturn(List.of(sampleOrderItem));

        mockMvc.perform(get("/order/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].order.orderId").value(1))
                .andExpect(jsonPath("$.results[0].order.username").value("tester"))
                .andExpect(jsonPath("$.results[0].orderItems[0].productName").value("Pizza Margherita"));
    }

    @Test
    void testCreateOrder() throws Exception {
        OrderAndItemInput input = new OrderAndItemInput();
        // กำหนดค่า input ตามที่ต้องการ

        when(orderService.createOrderAndOrderItems(any(OrderAndItemInput.class), any(Long.class)))
                .thenReturn("success");
        when(cartItemService.clearAllCartItem(any(Long.class))).thenReturn("success");

        mockMvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .requestAttr("profile_id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("create order and clear cart success"));
    }

    @Test
    void testUpdateOrder() throws Exception {
        OrderInput input = new OrderInput();
        // กำหนดค่า input ตามที่ต้องการ

        when(orderService.updateOrder(any(OrderInput.class), any(String.class))).thenReturn("success");

        mockMvc.perform(post("/order/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .requestAttr("username", "tester"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Update order success"));
    }
}
