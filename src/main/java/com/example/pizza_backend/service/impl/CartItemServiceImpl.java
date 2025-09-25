package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.CartItemDto;
import com.example.pizza_backend.api.dto.input.CartItemInput;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.persistence.entity.Cart;
import com.example.pizza_backend.persistence.entity.CartItem;
import com.example.pizza_backend.persistence.entity.Product;
import com.example.pizza_backend.persistence.repository.CartItemRepository;
import com.example.pizza_backend.persistence.repository.CartRepository;
import com.example.pizza_backend.persistence.repository.ProductRepository;
import com.example.pizza_backend.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final Mapper mapper;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository,
                               CartRepository cartRepository,
                               ProductRepository productRepository,
                               Mapper mapper) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CartItemDto> getCartItemsByCartId(Long cartId) {
        List<CartItem> cartItems = cartItemRepository.findByCartCartId(cartId);
        return cartItems.stream()
                .map(c->CartItemDto.builder()
                        .cartItemId(c.getCartItemId())
                        .cartId(c.getCart().getCartId())
                        .productName(c.getProduct().getProductName())
                        .productPrice(c.getProduct().getProductPrice())
                        .qty(c.getQty())
                        .lineTotal(c.getLineTotal())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public String createCartItem(CartItemInput cartItemInput, Long profileId) {
        Optional<Cart> cart = cartRepository.findByProfileProfileId(profileId);
        if (cart.isEmpty()){
            return "not found cart";
        }
        Optional<Product> product = productRepository.findById(cartItemInput.getProductId());
        if (product.isEmpty()){
            return "not found product";
        }

        CartItem cartItem = mapper.toCartItem(cartItemInput);
        cartItem.setCart(cart.get());
        cartItem.setProduct(product.get());

        cartItemRepository.save(cartItem);
        return "create items successfully";
    }
}
