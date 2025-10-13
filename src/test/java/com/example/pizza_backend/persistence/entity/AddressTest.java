package com.example.pizza_backend.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AddressTest {

    @Test
    void testBuilderAndGetters() {
        Address address = Address.builder()
                .addressId(1L)
                .phone("0123456789")
                .province("Bangkok")
                .amphor("Bang Kapi")
                .district("Hua Mak")
                .zipCode("10240")
                .addrNum("123/45")
                .detail("Near the park")
                .receivedName("Mr. Daraporn")
                .build();

        assertThat(address.getAddressId()).isEqualTo(1L);
        assertThat(address.getPhone()).isEqualTo("0123456789");
        assertThat(address.getProvince()).isEqualTo("Bangkok");
        assertThat(address.getAmphor()).isEqualTo("Bang Kapi");
        assertThat(address.getDistrict()).isEqualTo("Hua Mak");
        assertThat(address.getZipCode()).isEqualTo("10240");
        assertThat(address.getAddrNum()).isEqualTo("123/45");
        assertThat(address.getDetail()).isEqualTo("Near the park");
        assertThat(address.getReceivedName()).isEqualTo("Mr. Daraporn");
    }

    @Test
    void testSetterAndGetters() {
        Address address = new Address();
        address.setPhone("0987654321");
        address.setProvince("Chiang Mai");
        address.setAmphor("Mueang");
        address.setDistrict("Suthep");
        address.setZipCode("50200");
        address.setAddrNum("67/8");
        address.setDetail("Near the temple");
        address.setReceivedName("Ms. Somchai");

        assertThat(address.getPhone()).isEqualTo("0987654321");
        assertThat(address.getProvince()).isEqualTo("Chiang Mai");
        assertThat(address.getAmphor()).isEqualTo("Mueang");
        assertThat(address.getDistrict()).isEqualTo("Suthep");
        assertThat(address.getZipCode()).isEqualTo("50200");
        assertThat(address.getAddrNum()).isEqualTo("67/8");
        assertThat(address.getDetail()).isEqualTo("Near the temple");
        assertThat(address.getReceivedName()).isEqualTo("Ms. Somchai");
    }

    @Test
    void testEqualsAndHashCode() {
        Address addr1 = Address.builder().addressId(1L).build();
        Address addr2 = Address.builder().addressId(1L).build();

        assertThat(addr1).isEqualTo(addr2);
        assertThat(addr1.hashCode()).isEqualTo(addr2.hashCode());
    }

    @Test
    void testToString() {
        Address address = Address.builder()
                .addressId(1L)
                .phone("0123456789")
                .build();

        String str = address.toString();
        assertThat(str).contains("addressId=1", "phone=0123456789");
    }
}
