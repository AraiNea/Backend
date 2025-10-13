package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.AddressDto;
import com.example.pizza_backend.api.dto.input.AddressInput;
import com.example.pizza_backend.service.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
class AddressControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Autowired
    private ObjectMapper objectMapper;

    private AddressDto sampleAddress;

    @BeforeEach
    void setUp() {
        sampleAddress = AddressDto.builder()
                .addressId(1L)
                .phone("0812345678")
                .province("Bangkok")
                .amphor("Bang Kapi")
                .district("Hua Mak")
                .zipCode("10240")
                .addrNum("123/45")
                .detail("Near Mall")
                .receivedName("Mr. Test")
                .build();
    }

    @Test
    void testGetAddress() throws Exception {
        when(addressService.getAddressByProfileId(1L)).thenReturn(sampleAddress);

        mockMvc.perform(get("/address/list")
                        .requestAttr("profile_id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address.addressId").value(1))
                .andExpect(jsonPath("$.address.phone").value("0812345678"))
                .andExpect(jsonPath("$.address.province").value("Bangkok"))
                .andExpect(jsonPath("$.address.addrNum").value("123/45"))
                .andExpect(jsonPath("$.address.receivedName").value("Mr. Test"));
    }

    @Test
    void testUpdateAddressSuccess() throws Exception {
        AddressInput input = new AddressInput();
        input.setAddressId(1L);
        input.setPhone("0898765432");
        input.setProvince("Nonthaburi");
        input.setAmphor("Mueang");
        input.setDistrict("Sriracha");
        input.setZipCode("11000");
        input.setAddrNum("456/78");
        input.setDetail("Near Park");
        input.setReceivedName("Mrs. Update");

        when(addressService.updateAddress(any(AddressInput.class))).thenReturn("success");

        mockMvc.perform(post("/address/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Update address successfully"));
    }

    @Test
    void testUpdateAddressFailure() throws Exception {
        AddressInput input = new AddressInput();
        input.setAddressId(1L);
        input.setPhone("0898765432");

        when(addressService.updateAddress(any(AddressInput.class))).thenReturn("fail");

        mockMvc.perform(post("/address/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }
}
