package com.example.pizza_backend.api.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AddressDtoTest {

    @Test
    void testBuilderAndGetters() {
        AddressDto dto = AddressDto.builder()
                .addressId(1L)
                .phone("0123456789")
                .province("Bangkok")
                .amphor("Bangkok")
                .district("Bangkok")
                .zipCode("10100")
                .addrNum("123/45")
                .detail("Near BTS")
                .receivedName("Somchai")
                .build();

        assertThat(dto.getAddressId()).isEqualTo(1L);
        assertThat(dto.getPhone()).isEqualTo("0123456789");
        assertThat(dto.getProvince()).isEqualTo("Bangkok");
        assertThat(dto.getAmphor()).isEqualTo("Bangkok");
        assertThat(dto.getDistrict()).isEqualTo("Bangkok");
        assertThat(dto.getZipCode()).isEqualTo("10100");
        assertThat(dto.getAddrNum()).isEqualTo("123/45");
        assertThat(dto.getDetail()).isEqualTo("Near BTS");
        assertThat(dto.getReceivedName()).isEqualTo("Somchai");
    }




    @Test
    void testEqualsAndHashCode() {
        AddressDto dto1 = AddressDto.builder().addressId(1L).build();
        AddressDto dto2 = AddressDto.builder().addressId(1L).build();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void testToString() {
        AddressDto dto = AddressDto.builder()
                .addressId(1L)
                .phone("0123456789")
                .build();

        String str = dto.toString();
        assertThat(str).contains("addressId=1", "phone=0123456789");
    }
}
