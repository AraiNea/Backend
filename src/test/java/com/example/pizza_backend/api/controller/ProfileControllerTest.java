package com.example.pizza_backend.api.controller;

import com.example.pizza_backend.api.dto.ProfileDto;
import com.example.pizza_backend.api.dto.input.LoginInput;
import com.example.pizza_backend.api.dto.input.ProfileInput;
import com.example.pizza_backend.persistence.entity.Profile;
import com.example.pizza_backend.service.JwtService;
import com.example.pizza_backend.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProfileControllerTest {

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private ProfileService profileService;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signIn_shouldReturnSuccess_whenCredentialsCorrect() {
        LoginInput input = new LoginInput();
        input.setUsername("user1");
        input.setPassword("pass");

        Profile profile = new Profile();
        profile.setProfileId(1L);
        profile.setProfileName("user1");
        profile.setProfileRole(1);

        when(profileService.checkLogIn(input)).thenReturn(Optional.of(profile));
        when(jwtService.generateToken(anyMap())).thenReturn("token123");

        ResponseEntity<?> response = profileController.signIn(input);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("message")).isEqualTo("success");
        assertThat(response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0)).contains("token123");

        verify(profileService).checkLogIn(input);
        verify(jwtService).generateToken(anyMap());
    }

    @Test
    void signIn_shouldReturn401_whenCredentialsIncorrect() {
        LoginInput input = new LoginInput();
        input.setUsername("user1");
        input.setPassword("wrong");

        when(profileService.checkLogIn(input)).thenReturn(Optional.empty());

        ResponseEntity<?> response = profileController.signIn(input);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(401);
        assertThat(body.get("message")).isEqualTo("incorrect username or password");
    }

    @Test
    void userSignUp_shouldReturnSuccess_whenUsernameNotExists() {
        ProfileInput input = new ProfileInput();
        input.setUsername("user1");

        when(profileService.checkDuplicateProfile(input)).thenReturn(false);
        when(profileService.createProfileWithAddress(input, 1)).thenReturn("token123");

        ResponseEntity<?> response = profileController.userSignUp(input);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("message")).isEqualTo("signup success");
        assertThat(response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0)).contains("token123");

        verify(profileService).checkDuplicateProfile(input);
        verify(profileService).createProfileWithAddress(input, 1);
    }

    @Test
    void userSignUp_shouldReturn401_whenUsernameExists() {
        ProfileInput input = new ProfileInput();
        input.setUsername("user1");

        when(profileService.checkDuplicateProfile(input)).thenReturn(true);

        ResponseEntity<?> response = profileController.userSignUp(input);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCodeValue()).isEqualTo(401);
        assertThat(body.get("message")).isEqualTo("username already exists");
    }

    @Test
    void adminSignUp_shouldReturnSuccess_whenUsernameNotExists() {
        ProfileInput input = new ProfileInput();
        input.setUsername("admin");

        when(profileService.checkDuplicateProfile(input)).thenReturn(false);
        when(profileService.createProfileWithAddress(input, 2)).thenReturn("tokenAdmin");

        ResponseEntity<?> response = profileController.adminSighIn(input);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("message")).isEqualTo("signup success");
        assertThat(response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0)).contains("tokenAdmin");

        verify(profileService).checkDuplicateProfile(input);
        verify(profileService).createProfileWithAddress(input, 2);
    }

    @Test
    void logout_shouldReturnSuccess_andCookieCleared() {
        ResponseEntity<?> response = profileController.logout();
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("message")).isEqualTo("log out success");
        assertThat(response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0)).contains("Max-Age=0");
    }

    @Test
    void updateProfile_shouldReturnSuccess() {
        ProfileInput input = new ProfileInput();

        when(request.getAttribute("profile_id")).thenReturn(1L);
        when(profileService.updateProfile(input, 1L)).thenReturn("success");

        ResponseEntity<?> response = profileController.updateProfile(request, input);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("message")).isEqualTo("update success");

        verify(profileService).updateProfile(input, 1L);
    }

    @Test
    void updateProfile_shouldReturnBadRequest_whenFails() {
        ProfileInput input = new ProfileInput();

        when(request.getAttribute("profile_id")).thenReturn(1L);
        when(profileService.updateProfile(input, 1L)).thenReturn("error");

        ResponseEntity<?> response = profileController.updateProfile(request, input);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        verify(profileService).updateProfile(input, 1L);
    }

    @Test
    void getProfile_shouldReturnProfileDto() {
        ProfileDto dto = new ProfileDto();
        when(request.getAttribute("profile_id")).thenReturn(1L);
        when(profileService.getProfileById(1L)).thenReturn(dto);

        ResponseEntity<?> response = profileController.getProfile(request);
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body.get("profile")).isEqualTo(dto);
        verify(profileService).getProfileById(1L);
    }
}
