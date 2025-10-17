package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.OrderDto;
import com.example.pizza_backend.api.dto.input.CartItemInput;
import com.example.pizza_backend.api.dto.input.OrderAndItemInput;
import com.example.pizza_backend.api.dto.input.OrderInput;
import com.example.pizza_backend.api.dto.search.OrderSearchReq;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.exception.IdNotFoundException;
import com.example.pizza_backend.persistence.entity.*;
import com.example.pizza_backend.persistence.repository.*;
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
    private ProductRepository productRepository;
    private Mapper mapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            Mapper mapper,
                            ProfileRepository profileRepository,
                            AddressRepository addressRepository,
                            OrderItemRepository orderItemRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
        this.profileRepository = profileRepository;
        this.addressRepository = addressRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }
    public List<OrderDto> getOrdersByProfileId(Long profileId) {
        List<Orders> orders = orderRepository.getOrdersByProfileProfileId(profileId);
        return orders.stream()
                .map(order -> mapper.toOrderDto(order))
                .toList();
    }

    @Override
    public List<OrderDto> getAllOrders(OrderSearchReq req) {
        List<Orders> orders = orderRepository.searchOrder(req.getOrderId(), req.getStatus());
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
        Long addressId = profile.getAddress().getAddressId();
        Address address = addressRepository.findById(addressId)
                .orElseThrow(()-> new IdNotFoundException("Address not found"));

        Orders order = mapper.toOrder(orderInput);
        order.setAddress(address);
        order.setProfile(profile);
        orderRepository.save(order);

        for (CartItemInput cartItemInput : cartItemInputs) {
            OrderItem orderItem = mapper.toOrderItem(cartItemInput);
            orderItem.setOrder(order);
            orderItemRepository.save(orderItem);

            //ลด stock
            Product product = productRepository.findById(cartItemInput.getProductId())
                    .orElseThrow(() -> new IdNotFoundException("Product not found"));

            Integer currentStock = product.getProductStock(); // สมมติว่ามี field ชื่อ stock ใน Product
            Integer qtyToDeduct = cartItemInput.getQty();

            if (currentStock == null || currentStock < qtyToDeduct) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getProductName());
            }

            product.setProductStock(currentStock - qtyToDeduct);
            productRepository.save(product);
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
