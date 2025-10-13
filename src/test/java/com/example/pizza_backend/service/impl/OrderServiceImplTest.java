package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.OrderDto;
import com.example.pizza_backend.api.dto.input.CartItemInput;
import com.example.pizza_backend.api.dto.input.OrderAndItemInput;
import com.example.pizza_backend.api.dto.input.OrderInput;
import com.example.pizza_backend.api.dto.search.OrderSearchReq;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.exception.IdNotFoundException;
import com.example.pizza_backend.persistence.entity.Address;
import com.example.pizza_backend.persistence.entity.OrderItem;
import com.example.pizza_backend.persistence.entity.Orders;
import com.example.pizza_backend.persistence.entity.Profile;
import com.example.pizza_backend.persistence.repository.AddressRepository;
import com.example.pizza_backend.persistence.repository.OrderItemRepository;
import com.example.pizza_backend.persistence.repository.OrderRepository;
import com.example.pizza_backend.persistence.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private Mapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getOrdersByProfileId_shouldReturnOrderDtoList() {
        // given
        Long profileId = 1L;

        // สร้าง profile จำลอง
        Profile profile = new Profile();
        profile.setProfileId(profileId);

        // สร้าง Orders entity แตกต่างกัน
        Orders order1 = Orders.builder()
                .orderId(1L)
                .profile(profile)
                .status(0)
                .subtotal(500)
                .deliveryFee(50)
                .grandTotal(550)
                .build();

        Orders order2 = Orders.builder()
                .orderId(2L)
                .profile(profile)
                .status(1)
                .subtotal(300)
                .deliveryFee(30)
                .grandTotal(330)
                .build();

        // สร้าง DTO จริง
        OrderDto dto1 = OrderDto.builder()
                .orderId(order1.getOrderId())
                .status(order1.getStatus())
                .subtotal(order1.getSubtotal())
                .deliveryFee(order1.getDeliveryFee())
                .grandTotal(order1.getGrandTotal())
                .build();

        OrderDto dto2 = OrderDto.builder()
                .orderId(order2.getOrderId())
                .status(order2.getStatus())
                .subtotal(order2.getSubtotal())
                .deliveryFee(order2.getDeliveryFee())
                .grandTotal(order2.getGrandTotal())
                .build();

        // mock repository + mapper
        when(orderRepository.getOrdersByProfileProfileId(profileId))
                .thenReturn(List.of(order1, order2));
        when(mapper.toOrderDto(order1)).thenReturn(dto1);
        when(mapper.toOrderDto(order2)).thenReturn(dto2);

        // when
        List<OrderDto> result = orderService.getOrdersByProfileId(profileId);

        // then
        assertThat(result).containsExactly(dto1, dto2);

        // verify
        verify(orderRepository).getOrdersByProfileProfileId(profileId);
        verify(mapper).toOrderDto(order1);
        verify(mapper).toOrderDto(order2);
    }


    @Test
    void getAllOrders_shouldReturnOrderDtoList() {
        OrderSearchReq req = new OrderSearchReq();
        req.setOrderId(1L);
        req.setStatus(1);

        Orders order = new Orders();
        OrderDto dto = mock(OrderDto.class);

        when(orderRepository.searchOrder(req.getOrderId(), req.getStatus())).thenReturn(List.of(order));
        when(mapper.toOrderDto(order)).thenReturn(dto);

        List<OrderDto> result = orderService.getAllOrders(req);

        assertThat(result).hasSize(1).contains(dto);
        verify(orderRepository).searchOrder(req.getOrderId(), req.getStatus());
        verify(mapper).toOrderDto(order);
    }

    @Test
    void createOrderAndOrderItems_shouldReturnSuccess() {
        Long profileId = 1L;

        Profile profile = new Profile();
        Address address = new Address();

        OrderInput orderInput = new OrderInput();
        CartItemInput cartItemInput = new CartItemInput();

        Orders order = new Orders();
        OrderItem orderItem = new OrderItem();

        OrderAndItemInput orderAndItemInput = new OrderAndItemInput();
        orderAndItemInput.setOrderInput(orderInput);
        orderAndItemInput.setCartItemInputs(List.of(cartItemInput));

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(addressRepository.findAddressByProfile(profile)).thenReturn(Optional.of(address));
        when(mapper.toOrder(orderInput)).thenReturn(order);
        when(mapper.toOrderItem(cartItemInput)).thenReturn(orderItem);

        String result = orderService.createOrderAndOrderItems(orderAndItemInput, profileId);

        assertThat(result).isEqualTo("success");
        verify(orderRepository).save(order);
        verify(orderItemRepository).save(orderItem);
    }

    @Test
    void createOrderAndOrderItems_shouldThrowException_whenProfileNotFound() {
        Long profileId = 99L;
        OrderAndItemInput input = new OrderAndItemInput();

        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () ->
                orderService.createOrderAndOrderItems(input, profileId));
    }

    @Test
    void updateOrder_shouldReturnSuccess() {
        OrderInput orderInput = new OrderInput();
        orderInput.setOrderId(1L);
        Orders order = new Orders();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        String result = orderService.updateOrder(orderInput, "admin");

        assertThat(result).isEqualTo("success");
        verify(mapper).updateOrderFromInput(orderInput, order, "admin");
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrder_shouldThrowException_whenOrderIdIsNull() {
        OrderInput orderInput = new OrderInput();

        assertThrows(IllegalArgumentException.class, () ->
                orderService.updateOrder(orderInput, "admin"));
    }

    @Test
    void updateOrder_shouldThrowException_whenOrderNotFound() {
        OrderInput orderInput = new OrderInput();
        orderInput.setOrderId(99L);

        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () ->
                orderService.updateOrder(orderInput, "admin"));
    }
}
