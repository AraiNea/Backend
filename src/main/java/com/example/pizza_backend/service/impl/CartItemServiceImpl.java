package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.CartItemDto;
import com.example.pizza_backend.api.dto.input.CartItemInput;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.exception.IdNotFoundException;
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
        Cart cart = cartRepository.findByProfileProfileId(profileId)
                .orElseThrow(() -> new IdNotFoundException("Cart not found for this user"));

        if (cartItemInput.getProductId() == null) {
            throw new IllegalArgumentException("The given product Id cannot be null");
        }

        Product product = productRepository.findById(cartItemInput.getProductId())
                .orElseThrow(() -> new IdNotFoundException("Product Not found"));

        //หาว่า product นี้มีอยู่ใน cart แล้วหรือยัง
        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartCartIdAndProductProductId(cart.getCartId(), product.getProductId());

        if (existingItemOpt.isPresent()) {
            //ถ้ามีอยู่แล้ว — set cartItemId ลงใน input แล้วไป update แทน
            CartItem existingItem = existingItemOpt.get();
            cartItemInput.setCartItemId(existingItem.getCartItemId());

            // อาจจะเพิ่ม qty เดิม + ใหม่
            cartItemInput.setQty(existingItem.getQty() + cartItemInput.getQty());

            return updateCartItem(cartItemInput, profileId);
        }

        CartItem cartItem = mapper.toCartItem(cartItemInput);
        cartItem.setCart(cart);
        cartItem.setProduct(product);

        cartItemRepository.save(cartItem);
        return "success";
    }

    @Override
    @Transactional
    public String updateCartItem(CartItemInput cartItemInput, Long profileId) {
        cartRepository.findByProfileProfileId(profileId)
                .orElseThrow(() -> new IdNotFoundException("Cart not found for this user"));

        if (cartItemInput.getCartItemId() == null) {
            throw new IllegalArgumentException("The given cart item Id cannot be null");
        }
        CartItem cartItem = cartItemRepository.findById(cartItemInput.getCartItemId())
                .orElseThrow(() -> new IdNotFoundException("Cart Item Not found"));

        List<Long> allCartItemId = cartItemRepository.findCartItemIdsByProfileId(profileId);
        if (!allCartItemId.contains(cartItemInput.getCartItemId())) {
            throw new IllegalArgumentException("Access denied: this cart item does not belong to the current user.");
        }

        mapper.updateCartItemFromInput(cartItemInput, cartItem);
        cartItemRepository.save(cartItem);

        return "success";
    }

    @Override
    public String deleteCartItem(CartItemInput cartItemInput, Long profileId) {
        cartRepository.findByProfileProfileId(profileId)
                .orElseThrow(() -> new IdNotFoundException("Cart not found for this user"));

        if (cartItemInput.getCartItemId() == null) {
            throw new IllegalArgumentException("The given cart item Id cannot be null");
        }
        List<Long> allCartItemId = cartItemRepository.findCartItemIdsByProfileId(profileId);
        if (!allCartItemId.contains(cartItemInput.getCartItemId())) {
            throw new IllegalArgumentException("Access denied: this cart item does not belong to the current user.");
        }
        cartItemRepository.deleteById(cartItemInput.getCartItemId());
        return "success";
    }
}
