package com.example.pizza_backend.persistence.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileTest {

    @Test
    void testBuilderAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        Address address = Address.builder().addressId(1L).build();
        Cart cart = Cart.builder().cartId(2L).build();

        Profile profile = Profile.builder()
                .profileId(100L)
                .profileName("Daraporn")
                .profileSname("Saepoo")
                .profileRole(1)
                .address(address)
                .cart(cart)
                .username("daraporn")
                .password("secret")
                .createdAt(now)
                .build();

        assertThat(profile.getProfileId()).isEqualTo(100L);
        assertThat(profile.getProfileName()).isEqualTo("Daraporn");
        assertThat(profile.getProfileSname()).isEqualTo("Saepoo");
        assertThat(profile.getProfileRole()).isEqualTo(1);
        assertThat(profile.getAddress()).isEqualTo(address);
        assertThat(profile.getCart()).isEqualTo(cart);
        assertThat(profile.getUsername()).isEqualTo("daraporn");
        assertThat(profile.getPassword()).isEqualTo("secret");
        assertThat(profile.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testSetterAndGetters() {
        Profile profile = new Profile();
        LocalDateTime now = LocalDateTime.of(2025, 10, 10, 12, 0);
        Address address = Address.builder().addressId(2L).build();
        Cart cart = Cart.builder().cartId(3L).build();

        profile.setProfileId(101L);
        profile.setProfileName("Somchai");
        profile.setProfileSname("Yim");
        profile.setProfileRole(2);
        profile.setAddress(address);
        profile.setCart(cart);
        profile.setUsername("somchai");
        profile.setPassword("1234");
        profile.setCreatedAt(now);

        assertThat(profile.getProfileId()).isEqualTo(101L);
        assertThat(profile.getProfileName()).isEqualTo("Somchai");
        assertThat(profile.getProfileSname()).isEqualTo("Yim");
        assertThat(profile.getProfileRole()).isEqualTo(2);
        assertThat(profile.getAddress()).isEqualTo(address);
        assertThat(profile.getCart()).isEqualTo(cart);
        assertThat(profile.getUsername()).isEqualTo("somchai");
        assertThat(profile.getPassword()).isEqualTo("1234");
        assertThat(profile.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testOrdersList() {
        Profile profile = new Profile();
        List<Orders> orders = new ArrayList<>();
        Orders order1 = Orders.builder().orderId(1L).build();
        Orders order2 = Orders.builder().orderId(2L).build();
        orders.add(order1);
        orders.add(order2);

        profile.setOrders(orders);
        assertThat(profile.getOrders()).hasSize(2).contains(order1, order2);

        // Test adding/removing orders
        Orders order3 = Orders.builder().orderId(3L).build();
        profile.getOrders().add(order3);
        assertThat(profile.getOrders()).hasSize(3).contains(order3);

        profile.getOrders().remove(order1);
        assertThat(profile.getOrders()).hasSize(2).doesNotContain(order1);
    }

    @Test
    void testEqualsAndHashCode() {
        Profile p1 = Profile.builder().profileId(1L).build();
        Profile p2 = Profile.builder().profileId(1L).build();

        assertThat(p1).isEqualTo(p2);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }

    @Test
    void testToString() {
        Profile profile = Profile.builder()
                .profileId(1L)
                .profileName("Daraporn")
                .username("daraporn")
                .build();

        String str = profile.toString();
        assertThat(str).contains("profileId=1", "profileName=Daraporn", "username=daraporn");
    }
}
