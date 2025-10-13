package com.example.pizza_backend.api.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileDtoTest {

    @Test
    void testSetterAndGetters() {
        ProfileDto dto = new ProfileDto();
        dto.setProfileName("Daraporn");
        dto.setProfileSname("Saepoo");
        dto.setUsername("daraporn123");
        dto.setPassword("secret");

        assertThat(dto.getProfileName()).isEqualTo("Daraporn");
        assertThat(dto.getProfileSname()).isEqualTo("Saepoo");
        assertThat(dto.getUsername()).isEqualTo("daraporn123");
        assertThat(dto.getPassword()).isEqualTo("secret");
    }

    @Test
    void testEqualsAndHashCode() {
        ProfileDto dto1 = new ProfileDto();
        dto1.setUsername("daraporn123");

        ProfileDto dto2 = new ProfileDto();
        dto2.setUsername("daraporn123");

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void testToString() {
        ProfileDto dto = new ProfileDto();
        dto.setProfileName("Daraporn");
        dto.setUsername("daraporn123");

        String str = dto.toString();
        assertThat(str).contains("profileName=Daraporn", "username=daraporn123");
    }
}
