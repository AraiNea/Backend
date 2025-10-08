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
import com.example.pizza_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private ProfileRepository profileRepository;
    private AddressRepository addressRepository;
    private OrderItemRepository orderItemRepository;
    private Mapper mapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            Mapper mapper,
                            ProfileRepository profileRepository,
                            AddressRepository addressRepository,
                            OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
        this.profileRepository = profileRepository;
        this.addressRepository = addressRepository;
        this.orderItemRepository = orderItemRepository;
    }
    public List<OrderDto> getOrdersByProfileId(Long profileId) {
        List<Orders> orders = orderRepository.getOrdersByProfileProfileId(profileId);
        return orders.stream()
                .map(order -> mapper.toOrderDto(order))
                .toList();
    }

    @Override
    public List<OrderDto> getAllOrders(OrderSearchReq req) {
        List<Orders> orders = orderRepository.searchOrder(req.getOrderId());
        return orders.stream()
                .map(order -> mapper.toOrderDto(order))
                .toList();
    }

    @Override
    @Transactional
    public String createOrderAndOrderItems(OrderAndItemInput orderAndItemInput, Long profileId) {
        OrderInput orderInput = orderAndItemInput.getOrderInput();
        List<CartItemInput> cartItemInputs = orderAndItemInput.getCartItemInputs();
        Profile profile =  profileRepository.findById(profileId)
                .orElseThrow(()-> new IdNotFoundException("Profile not found"));
        Address address = addressRepository.findAddressByProfile(profile)
                .orElseThrow(()-> new IdNotFoundException("Address not found"));

        Orders order = mapper.toOrder(orderInput);
        order.setAddress(address);
        order.setProfile(profile);
        orderRepository.save(order);

        for (CartItemInput cartItemInput : cartItemInputs) {
            OrderItem orderItem = mapper.toOrderItem(cartItemInput);
            orderItem.setOrder(order);
            orderItemRepository.save(orderItem);
        }
        return "success";
    }

    @Override
    @Transactional
    public String updateOrder(OrderInput orderInput, String name) {
        if (orderInput.getOrderId() == null) {
            throw new IllegalArgumentException("The given Order ID cannot be null");
        }
        Orders order = orderRepository.findById(orderInput.getOrderId())
                .orElseThrow(()-> new IdNotFoundException("Order not found"));
        mapper.updateOrderFromInput(orderInput, order, name);
        orderRepository.save(order);
        return "success";
    }
}
