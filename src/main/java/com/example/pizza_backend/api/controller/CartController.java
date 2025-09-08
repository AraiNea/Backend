package com.example.pizza_backend.api.controller;


import com.example.pizza_backend.api.dto.CartDto;
import com.example.pizza_backend.api.dto.CartItemDto;
import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.input.CartItemInput;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.service.CartItemService;
import com.example.pizza_backend.service.CartService;
import com.example.pizza_backend.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    @Autowired
    public CartController(CartService cartService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }


    @GetMapping("/list")
    public ResponseEntity<?> getCart(HttpServletRequest request) {
        // ดึง profile_id ที่ถูก set มาจาก Interceptor
        Long profileId = (Long) request.getAttribute("profile_id");

        CartDto cart = cartService.getCartDtoByProfileId(profileId);
        List<CartItemDto> cartItems = cartItemService.getCartItemsByCartId(cart.getCartId());

        //map cart,cartItem
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("cart", cart);
        response.put("cartItems", cartItems);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/addItems")
    public ResponseEntity<?> addItem(HttpServletRequest request, @RequestBody CartItemInput cartItemInput) {
        Long profileId = (Long) request.getAttribute("profile_id");
        String createLog = cartItemService.createCartItem(cartItemInput, profileId);

        return ResponseEntity.ok()
                .body(Map.of("message", createLog));
    }

    @PostMapping("/updateItems")
    public ResponseEntity<?> updateItem(HttpServletRequest request, @RequestBody CartItemInput cartItemInput) {
        Long profileId = (Long) request.getAttribute("profile_id");
        String updateLog = cartItemService.createCartItem(cartItemInput, profileId);

        return ResponseEntity.ok()
                .body(Map.of("message", updateLog));
    }

}
