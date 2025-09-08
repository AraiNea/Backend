package com.example.pizza_backend.api.mapper;
import com.example.pizza_backend.api.dto.input.ProfileInputReq;
import com.example.pizza_backend.persistence.entity.Address;
import com.example.pizza_backend.persistence.entity.Profile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    // Mapping จาก DTO → Entity
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "profileRole", expression = "java(role)")
    @Mapping(target = "address", ignore = true)
    Profile toProfile(ProfileInputReq req, @Context Integer role);

    Address toAddress(ProfileInputReq req);

    // กรณี map กลับก็ทำได้
    // ProfileInputReq toDto(Profile profile);
}

