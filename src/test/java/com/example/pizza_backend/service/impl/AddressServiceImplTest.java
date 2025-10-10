package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.AddressDto;
import com.example.pizza_backend.api.dto.input.AddressInput;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.exception.IdNotFoundException;
import com.example.pizza_backend.persistence.entity.Address;
import com.example.pizza_backend.persistence.entity.Profile;
import com.example.pizza_backend.persistence.repository.AddressRepository;
import com.example.pizza_backend.persistence.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AddressServiceImplTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private Mapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ======== getAddressByProfileId ========

    @Test
    void getAddressByProfileId_shouldReturnAddressDto() {
        Profile profile = new Profile();
        Address address = new Address();
        AddressDto addressDto = mock(AddressDto.class);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(addressRepository.findAddressByProfile(profile)).thenReturn(Optional.of(address));
        when(mapper.toAddressDto(address)).thenReturn(addressDto);

        AddressDto result = addressService.getAddressByProfileId(1L);
        assertThat(result).isEqualTo(addressDto);

        verify(profileRepository).findById(1L);
        verify(addressRepository).findAddressByProfile(profile);
        verify(mapper).toAddressDto(address);
    }

    @Test
    void getAddressByProfileId_shouldThrowException_whenProfileNotFound() {
        when(profileRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IdNotFoundException.class, () -> addressService.getAddressByProfileId(1L));
        verify(profileRepository).findById(1L);
    }

    @Test
    void getAddressByProfileId_shouldThrowException_whenAddressNotFound() {
        Profile profile = new Profile();
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(addressRepository.findAddressByProfile(profile)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> addressService.getAddressByProfileId(1L));

        verify(profileRepository).findById(1L);
        verify(addressRepository).findAddressByProfile(profile);
    }

    @Test
    void getAddressByProfileId_shouldThrowException_whenProfileIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> addressService.getAddressByProfileId(null));
    }

    // ======== updateAddress ========

    @Test
    void updateAddress_shouldReturnSuccess() {
        AddressInput input = new AddressInput();
        input.setAddressId(1L);

        Address address = new Address();

        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        doNothing().when(mapper).updateAddressFromInput(input, address);
        when(addressRepository.save(address)).thenReturn(address);

        String result = addressService.updateAddress(input);
        assertThat(result).isEqualTo("success");

        verify(addressRepository).findById(1L);
        verify(mapper).updateAddressFromInput(input, address);
        verify(addressRepository).save(address);
    }

    @Test
    void updateAddress_shouldThrowException_whenAddressIdIsNull() {
        AddressInput input = new AddressInput();
        input.setAddressId(null);
        assertThrows(IllegalArgumentException.class, () -> addressService.updateAddress(input));
    }

    @Test
    void updateAddress_shouldThrowException_whenAddressNotFound() {
        AddressInput input = new AddressInput();
        input.setAddressId(1L);
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> addressService.updateAddress(input));
        verify(addressRepository).findById(1L);
    }
}
