package com.example.pizza_backend.api.mapper;
import com.example.pizza_backend.api.dto.input.CartItemInput;
import com.example.pizza_backend.api.dto.input.ProductInput;
import com.example.pizza_backend.api.dto.input.ProfileInput;
import com.example.pizza_backend.persistence.entity.Address;
import com.example.pizza_backend.persistence.entity.CartItem;
import com.example.pizza_backend.persistence.entity.Profile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    // Mapping จาก DTO → Entity
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "profileRole", expression = "java(role)")
    @Mapping(target = "address", ignore = true)
    Profile toProfile(ProfileInput req, @Context Integer role);

    Address toAddress(ProfileInput req);

    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", ignore = true)
    CartItem toCartItem(CartItemInput cartItemInput);

    // กรณี map กลับก็ทำได้
    // ProfileInputReq toDto(Profile profile);
}

