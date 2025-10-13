package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.api.dto.input.LoginInput;
import com.example.pizza_backend.api.dto.input.ProfileInput;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.persistence.entity.Address;
import com.example.pizza_backend.persistence.entity.Cart;
import com.example.pizza_backend.persistence.entity.Profile;
import com.example.pizza_backend.persistence.repository.AddressRepository;
import com.example.pizza_backend.persistence.repository.CartRepository;
import com.example.pizza_backend.persistence.repository.ProfileRepository;
import com.example.pizza_backend.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProfileServiceImplTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private Mapper mapper;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ==================== checkLogIn ====================
    @Test
    void checkLogIn_shouldReturnProfile_whenFound() {
        LoginInput input = new LoginInput();
        input.setUsername("user");
        input.setPassword("pass");

        Profile profile = new Profile();
        profile.setProfileId(1L);

        when(profileRepository.findFirstByUsernameAndPassword("user", "pass"))
                .thenReturn(Optional.of(profile));

        Optional<Profile> result = profileService.checkLogIn(input);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getProfileId());
    }

    @Test
    void checkLogIn_shouldThrowException_whenUsernameIsNull() {
        LoginInput input = new LoginInput();
        input.setPassword("pass");

        assertThrows(IllegalArgumentException.class,
                () -> profileService.checkLogIn(input));
    }

    @Test
    void checkLogIn_shouldThrowException_whenPasswordIsNull() {
        LoginInput input = new LoginInput();
        input.setUsername("user");

        assertThrows(IllegalArgumentException.class,
                () -> profileService.checkLogIn(input));
    }

    // ==================== checkDuplicateProfile ====================
    @Test
    void checkDuplicateProfile_shouldReturnTrue_whenExists() {
        ProfileInput input = new ProfileInput();
        input.setUsername("user");

        when(profileRepository.existsByUsername("user")).thenReturn(true);

        assertTrue(profileService.checkDuplicateProfile(input));
    }

    @Test
    void checkDuplicateProfile_shouldReturnFalse_whenNotExists() {
        ProfileInput input = new ProfileInput();
        input.setUsername("user");

        when(profileRepository.existsByUsername("user")).thenReturn(false);

        assertFalse(profileService.checkDuplicateProfile(input));
    }

    @Test
    void checkDuplicateProfile_shouldThrowException_whenUsernameIsNull() {
        ProfileInput input = new ProfileInput();

        assertThrows(IllegalArgumentException.class,
                () -> profileService.checkDuplicateProfile(input));
    }

    // ==================== createProfileWithAddress ====================
    @Test
    void createProfileWithAddress_shouldCreateUserWithCartAndAddress_whenRoleIsUser() {
        ProfileInput input = new ProfileInput();
        input.setUsername("user");

        Profile mockProfile = new Profile();
        mockProfile.setProfileId(10L);
        mockProfile.setUsername("user");

        Address mockAddress = new Address();

        when(mapper.toProfile(any(), eq(1))).thenReturn(mockProfile);
        when(mapper.toAddress(any())).thenReturn(mockAddress);
        when(jwtService.generateToken(any(Map.class))).thenReturn("token123");

        String token = profileService.createProfileWithAddress(input, 1);

        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(addressRepository, times(1)).save(any(Address.class));
        verify(profileRepository, times(1)).save(mockProfile);
        assertEquals("token123", token);
    }

    @Test
    void createProfileWithAddress_shouldCreateAdminWithoutCartAndAddress_whenRoleIsAdmin() {
        ProfileInput input = new ProfileInput();
        input.setUsername("admin");

        Profile mockProfile = new Profile();
        mockProfile.setProfileId(20L);
        mockProfile.setUsername("admin");

        when(mapper.toProfile(any(), eq(2))).thenReturn(mockProfile);
        when(jwtService.generateToken(any(Map.class))).thenReturn("tokenAdmin");

        String token = profileService.createProfileWithAddress(input, 2);

        verify(cartRepository, never()).save(any());
        verify(addressRepository, never()).save(any());
        verify(profileRepository, times(1)).save(mockProfile);
        assertEquals("tokenAdmin", token);
    }
}
