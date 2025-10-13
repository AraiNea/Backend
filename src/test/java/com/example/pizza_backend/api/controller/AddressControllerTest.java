package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.AddressDto;
import com.example.pizza_backend.api.dto.input.AddressInput;
import com.example.pizza_backend.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AddressControllerTest {

    @InjectMocks
    private AddressController addressController;

    @Mock
    private AddressService addressService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAddress_shouldReturnAddressDto() {
        // ใช้ mock แทนการสร้าง AddressDto จริง
        AddressDto dto = mock(AddressDto.class);

        when(request.getAttribute("profile_id")).thenReturn(1L);
        when(addressService.getAddressByProfileId(1L)).thenReturn(dto);

        ResponseEntity<?> response = addressController.getAddress(request);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("address")).isEqualTo(dto);

        verify(addressService).getAddressByProfileId(1L);
    }

    @Test
    void updateAddress_shouldReturnSuccess_whenServiceReturnsSuccess() {
        AddressInput input = new AddressInput();
        when(addressService.updateAddress(input)).thenReturn("success");

        ResponseEntity<?> response = addressController.updateAddress(input);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("message")).isEqualTo("Update address successfully");

        verify(addressService).updateAddress(input);
    }

    @Test
    void updateAddress_shouldReturnBadRequest_whenServiceFails() {
        AddressInput input = new AddressInput();
        when(addressService.updateAddress(input)).thenReturn("error");

        ResponseEntity<?> response = addressController.updateAddress(input);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();

        verify(addressService).updateAddress(input);
    }
}
