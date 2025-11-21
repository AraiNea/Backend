package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.DiscountCodeDto;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private ProfileRepository profileRepository;
    private AddressRepository addressRepository;
    private OrderItemRepository orderItemRepository;
    private ProductRepository productRepository;
    private DiscountCodeRepository discountCodeRepository;
    private InventoryRepository inventoryRepository;
    private Mapper mapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            Mapper mapper,
                            ProfileRepository profileRepository,
                            AddressRepository addressRepository,
                            OrderItemRepository orderItemRepository,
                            ProductRepository productRepository,
                            DiscountCodeRepository discountCodeRepository,
                            InventoryRepository inventoryRepository) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
        this.profileRepository = profileRepository;
        this.addressRepository = addressRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.discountCodeRepository = discountCodeRepository;
        this.inventoryRepository = inventoryRepository;
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

        //ลด qty code ในกรณีที่มี
        if (orderAndItemInput.getDiscountCode() != null) {
            Long discountId = orderAndItemInput.getDiscountCode().getDiscountId();
            DiscountCode code = discountCodeRepository.findById(discountId)
                    .orElseThrow(() -> new IdNotFoundException("Code not found"));
            Integer currentQty = code.getQty();
            Integer codeQtyToDeduct = 1;
            if (currentQty == null || currentQty < codeQtyToDeduct) {
                throw new IllegalArgumentException("Code is out");
            }
            code.setQty(currentQty - codeQtyToDeduct);
            discountCodeRepository.save(code);
        }

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
                throw new IllegalArgumentException("Not enough stock");
            }

            product.setProductStock(currentStock - qtyToDeduct);
            productRepository.save(product);

            InventoryTransaction ivt = new InventoryTransaction();
            ivt.setProduct(product);
            ivt.setCreatedAt(LocalDateTime.now());
            ivt.setTransactionType(2);
            ivt.setQtyChange(qtyToDeduct);
            inventoryRepository.save(ivt);
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
