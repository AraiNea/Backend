package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.OrderDto;
import com.example.pizza_backend.api.dto.input.OrderAndItemInput;
import com.example.pizza_backend.api.dto.input.OrderInput;
import com.example.pizza_backend.api.dto.search.OrderSearchReq;
import com.example.pizza_backend.service.CartItemService;
import com.example.pizza_backend.service.OrderItemService;
import com.example.pizza_backend.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getOrders_shouldReturnListOfOrders() {
        OrderDto orderDto = mock(OrderDto.class);
        when(orderDto.getOrderId()).thenReturn(1L);
        when(request.getAttribute("profile_id")).thenReturn(1L);
        when(orderService.getOrdersByProfileId(1L)).thenReturn(List.of(orderDto));
        when(orderItemService.getOrderItems(1L)).thenReturn(List.of());

        ResponseEntity<?> response = orderController.getOrders(request);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).get("order")).isEqualTo(orderDto);

        verify(orderService).getOrdersByProfileId(1L);
        verify(orderItemService).getOrderItems(1L);
    }

    @Test
    void getAllOrders_shouldReturnListOfOrders() {
        OrderDto orderDto = mock(OrderDto.class);
        when(orderDto.getOrderId()).thenReturn(1L);
        OrderSearchReq req = new OrderSearchReq();
        when(orderService.getAllOrders(req)).thenReturn(List.of(orderDto));
        when(orderItemService.getOrderItems(1L)).thenReturn(List.of());

        ResponseEntity<?> response = orderController.getAllOrders(req);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).get("order")).isEqualTo(orderDto);

        verify(orderService).getAllOrders(req);
        verify(orderItemService).getOrderItems(1L);
    }

    @Test
    void createOrder_shouldReturnSuccess_whenServiceReturnsSuccess() {
        OrderAndItemInput input = new OrderAndItemInput();
        when(request.getAttribute("profile_id")).thenReturn(1L);
        when(orderService.createOrderAndOrderItems(input, 1L)).thenReturn("success");
        when(cartItemService.clearAllCartItem(1L)).thenReturn("success");

        ResponseEntity<?> response = orderController.createOrder(request, input);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("message")).isEqualTo("create order and clear cart success");

        verify(orderService).createOrderAndOrderItems(input, 1L);
        verify(cartItemService).clearAllCartItem(1L);
    }

    @Test
    void createOrder_shouldReturnBadRequest_whenServiceFails() {
        OrderAndItemInput input = new OrderAndItemInput();
        when(request.getAttribute("profile_id")).thenReturn(1L);
        when(orderService.createOrderAndOrderItems(input, 1L)).thenReturn("error");
        when(cartItemService.clearAllCartItem(1L)).thenReturn("success");

        ResponseEntity<?> response = orderController.createOrder(request, input);
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();

        verify(orderService).createOrderAndOrderItems(input, 1L);
        verify(cartItemService).clearAllCartItem(1L);
    }

    @Test
    void updateOrder_shouldReturnSuccess_whenServiceReturnsSuccess() {
        OrderInput input = new OrderInput();
        when(request.getAttribute("username")).thenReturn("admin");
        when(orderService.updateOrder(input, "admin")).thenReturn("success");

        ResponseEntity<?> response = orderController.updateOrder(request, input);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("message")).isEqualTo("Update order success");

        verify(orderService).updateOrder(input, "admin");
    }

    @Test
    void updateOrder_shouldReturnBadRequest_whenServiceFails() {
        OrderInput input = new OrderInput();
        when(request.getAttribute("username")).thenReturn("admin");
        when(orderService.updateOrder(input, "admin")).thenReturn("error");

        ResponseEntity<?> response = orderController.updateOrder(request, input);
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();

        verify(orderService).updateOrder(input, "admin");
    }
}
