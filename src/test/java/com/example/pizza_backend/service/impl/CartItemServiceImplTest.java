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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartItemServiceImplTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ------------------- getCartItemsByCartId -------------------

    @Test
    void testGetCartItemsByCartId_Success() {
        // Arrange
        Cart cart = new Cart();
        cart.setCartId(1L);

        Product product = new Product();
        product.setProductName("Pizza");
        product.setProductPrice(250); // Integer

        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(100L);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQty(2);
        cartItem.setLineTotal(500); // Integer

        // Mock DTO แทนการสร้าง instance จริง
        CartItemDto cartItemDto = mock(CartItemDto.class);
        when(cartItemDto.getCartItemId()).thenReturn(100L);
        when(cartItemDto.getProductName()).thenReturn("Pizza");
        when(cartItemDto.getProductPrice()).thenReturn(250);
        when(cartItemDto.getQty()).thenReturn(2);
        when(cartItemDto.getLineTotal()).thenReturn(500);

        when(cartItemRepository.findByCartCartId(1L)).thenReturn(List.of(cartItem));
        when(mapper.toCartItemDto(cartItem)).thenReturn(cartItemDto);

        // Act
        List<CartItemDto> result = cartItemService.getCartItemsByCartId(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getCartItemId());
        assertEquals("Pizza", result.get(0).getProductName());
        assertEquals(250, result.get(0).getProductPrice());
        assertEquals(2, result.get(0).getQty());
        assertEquals(500, result.get(0).getLineTotal());
        verify(cartItemRepository, times(1)).findByCartCartId(1L);
        verify(mapper, times(1)).toCartItemDto(cartItem);
    }

    // ------------------- createCartItem -------------------

    @Test
    void testCreateCartItem_Success() {
        Long profileId = 1L;
        Long productId = 10L;

        CartItemInput input = new CartItemInput();
        input.setProductId(productId);
        input.setQty(2);

        Cart cart = new Cart();
        cart.setCartId(100L);

        Product product = new Product();
        product.setProductId(productId);

        CartItem cartItem = new CartItem();

        when(cartRepository.findByProfileProfileId(profileId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(mapper.toCartItem(input)).thenReturn(cartItem);

        // Act
        String result = cartItemService.createCartItem(input, profileId);

        // Assert
        verify(cartItemRepository, times(1)).save(cartItem);
        assertEquals(cart, cartItem.getCart());
        assertEquals(product, cartItem.getProduct());
        assertEquals("success", result);
    }

    @Test
    void testCreateCartItem_CartNotFound() {
        Long profileId = 1L;
        CartItemInput input = new CartItemInput();
        input.setProductId(10L);

        when(cartRepository.findByProfileProfileId(profileId)).thenReturn(Optional.empty());

        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> cartItemService.createCartItem(input, profileId));

        assertEquals("Cart not found for this user", exception.getMessage());
    }

    @Test
    void testCreateCartItem_ProductNotFound() {
        Long profileId = 1L;
        Long productId = 10L;
        CartItemInput input = new CartItemInput();
        input.setProductId(productId);

        Cart cart = new Cart();

        when(cartRepository.findByProfileProfileId(profileId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> cartItemService.createCartItem(input, profileId));

        assertEquals("Product Not found", exception.getMessage());
    }

    @Test
    void testCreateCartItem_ProductIdNull() {
        Long profileId = 1L;
        CartItemInput input = new CartItemInput();
        input.setProductId(null);

        Cart cart = new Cart();

        when(cartRepository.findByProfileProfileId(profileId)).thenReturn(Optional.of(cart));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> cartItemService.createCartItem(input, profileId));

        assertEquals("The given product Id cannot be null", exception.getMessage());
    }
}
