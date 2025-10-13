package com.example.pizza_backend.api.mapper;

import com.example.pizza_backend.api.dto.*;
import com.example.pizza_backend.api.dto.input.*;
import com.example.pizza_backend.persistence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MapperImplTest {

    private MapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new MapperImpl();
    }

    @Test
    void testToProfile() {
        ProfileInput input = new ProfileInput();
        input.setProfileName("John");
        input.setProfileSname("Doe");
        input.setUsername("john.doe");
        input.setPassword("pass123");

        Profile profile = mapper.toProfile(input, 1);

        assertThat(profile.getProfileName()).isEqualTo("John");
        assertThat(profile.getProfileSname()).isEqualTo("Doe");
        assertThat(profile.getUsername()).isEqualTo("john.doe");
        assertThat(profile.getPassword()).isEqualTo("pass123");
        assertThat(profile.getProfileRole()).isEqualTo(1);
        assertThat(profile.getCreatedAt()).isNotNull();
    }

    @Test
    void testToAddress() {
        ProfileInput input = new ProfileInput();
        input.setPhone("12345");
        input.setProvince("Bangkok");
        input.setAmphor("Pathumwan");
        input.setDistrict("Pathumwan");
        input.setZipCode("10330");
        input.setAddrNum("10/1");
        input.setDetail("Near BTS");
        input.setReceivedName("John");

        Address address = mapper.toAddress(input);

        assertThat(address.getPhone()).isEqualTo("12345");
        assertThat(address.getProvince()).isEqualTo("Bangkok");
        assertThat(address.getAmphor()).isEqualTo("Pathumwan");
        assertThat(address.getDistrict()).isEqualTo("Pathumwan");
        assertThat(address.getZipCode()).isEqualTo("10330");
        assertThat(address.getAddrNum()).isEqualTo("10/1");
        assertThat(address.getDetail()).isEqualTo("Near BTS");
        assertThat(address.getReceivedName()).isEqualTo("John");
    }

    @Test
    void testToCartItem() {
        CartItemInput input = new CartItemInput();
        input.setQty(5);
        input.setLineTotal(500);

        CartItem cartItem = mapper.toCartItem(input);

        assertThat(cartItem.getQty()).isEqualTo(5);
        assertThat(cartItem.getLineTotal()).isEqualTo(500);
    }

    @Test
    void testUpdateCartItemFromInput() {
        CartItemInput input = new CartItemInput();
        input.setQty(10);
        input.setLineTotal(1000);

        CartItem cartItem = new CartItem();
        cartItem.setQty(5);
        cartItem.setLineTotal(500);

        mapper.updateCartItemFromInput(input, cartItem);

        assertThat(cartItem.getQty()).isEqualTo(10);
        assertThat(cartItem.getLineTotal()).isEqualTo(1000);
    }

    @Test
    void testUpdateProfileFromInput() {
        ProfileInput input = new ProfileInput();
        input.setProfileName("Alice");
        input.setProfileSname("Smith");
        input.setUsername("alice");
        input.setPassword("pwd");

        Profile profile = new Profile();
        mapper.updateProfileFromInput(input, profile);

        assertThat(profile.getProfileName()).isEqualTo("Alice");
        assertThat(profile.getProfileSname()).isEqualTo("Smith");
        assertThat(profile.getUsername()).isEqualTo("alice");
        assertThat(profile.getPassword()).isEqualTo("pwd");
    }

    @Test
    void testUpdateProductFromInput() {
        ProductInput input = new ProductInput();
        input.setProductName("Pizza");
        input.setProductDetail("Cheese");
        input.setProductPrice(100);
        input.setProductStock(50);
        input.setIsActive(1);

        Product product = new Product();
        mapper.updateProductFromInput(input, product, "admin");

        assertThat(product.getProductName()).isEqualTo("Pizza");
        assertThat(product.getProductDetail()).isEqualTo("Cheese");
        assertThat(product.getProductPrice()).isEqualTo(100);
        assertThat(product.getProductStock()).isEqualTo(50);
        assertThat(product.getIsActive()).isEqualTo(1);
        assertThat(product.getUpdatedBy()).isEqualTo("admin");
        assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    void testToProductDto() {
        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("Pizza");
        product.setProductDetail("Cheese");
        product.setProductPrice(200);
        product.setProductStock(10);
        product.setIsActive(1);
        product.setCreatedAt(LocalDateTime.of(2025, 10, 10, 12, 0));
        product.setUpdatedAt(LocalDateTime.of(2025, 10, 11, 12, 0));
        product.setCreatedBy("admin");
        product.setUpdatedBy("admin");

        ProductDto dto = mapper.toProductDto(product);

        assertThat(dto.getProductId()).isEqualTo(1L);
        assertThat(dto.getProductName()).isEqualTo("Pizza");
        assertThat(dto.getProductDetail()).isEqualTo("Cheese");
        assertThat(dto.getProductPrice()).isEqualTo(200);
        assertThat(dto.getProductStock()).isEqualTo(10);
        assertThat(dto.getIsActive()).isEqualTo(1);
        assertThat(dto.getCreatedAt()).isEqualTo(product.getCreatedAt().toLocalDate());
        assertThat(dto.getUpdatedAt()).isEqualTo(product.getUpdatedAt().toLocalDate());
        assertThat(dto.getCreatedBy()).isEqualTo("admin");
        assertThat(dto.getUpdatedBy()).isEqualTo("admin");
    }

    @Test
    void testToOrderDto() {
        Orders order = new Orders();
        order.setOrderId(1L);
        order.setStatus(1);
        order.setSubtotal(100);
        order.setDeliveryFee(10);
        order.setGrandTotal(110);
        order.setCreatedAt(LocalDateTime.of(2025, 10, 10, 12, 0));
        Profile profile = new Profile();
        profile.setUsername("user1");
        order.setProfile(profile);

        OrderDto dto = mapper.toOrderDto(order);

        assertThat(dto.getOrderId()).isEqualTo(1L);
        assertThat(dto.getStatus()).isEqualTo(1);
        assertThat(dto.getSubtotal()).isEqualTo(100);
        assertThat(dto.getDeliveryFee()).isEqualTo(10);
        assertThat(dto.getGrandTotal()).isEqualTo(110);
        assertThat(dto.getUsername()).isEqualTo("user1");
        assertThat(dto.getCreatedAt()).isEqualTo(order.getCreatedAt().toLocalDate());
    }

    // เพิ่ม test สำหรับ helper method ที่เป็น private ด้วย reflection
    @Test
    void testPrivateHelperMethods() throws Exception {
        Product product = new Product();
        product.setProductId(123L);
        product.setProductName("Pizza");
        product.setProductDetail("Cheese");
        product.setProductPrice(200);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);

        java.lang.reflect.Method m1 = MapperImpl.class.getDeclaredMethod("cartItemProductProductId", CartItem.class);
        m1.setAccessible(true);
        assertThat(m1.invoke(mapper, cartItem)).isEqualTo(123L);

        java.lang.reflect.Method m2 = MapperImpl.class.getDeclaredMethod("cartItemProductProductName", CartItem.class);
        m2.setAccessible(true);
        assertThat(m2.invoke(mapper, cartItem)).isEqualTo("Pizza");

        java.lang.reflect.Method m3 = MapperImpl.class.getDeclaredMethod("cartItemProductProductDetail", CartItem.class);
        m3.setAccessible(true);
        assertThat(m3.invoke(mapper, cartItem)).isEqualTo("Cheese");

        java.lang.reflect.Method m4 = MapperImpl.class.getDeclaredMethod("cartItemProductProductPrice", CartItem.class);
        m4.setAccessible(true);
        assertThat(m4.invoke(mapper, cartItem)).isEqualTo(200);
    }
}
