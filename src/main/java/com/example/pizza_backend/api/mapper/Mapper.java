package com.example.pizza_backend.api.mapper;
import com.example.pizza_backend.api.dto.input.CartItemInput;
import com.example.pizza_backend.api.dto.input.ProductInput;
import com.example.pizza_backend.api.dto.input.ProfileInput;
import com.example.pizza_backend.persistence.entity.Address;
import com.example.pizza_backend.persistence.entity.CartItem;
import com.example.pizza_backend.persistence.entity.Product;
import com.example.pizza_backend.persistence.entity.Profile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    // Mapping จาก DTO → Entity

    // CREATE
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "profileRole", expression = "java(role)")
    @Mapping(target = "address", ignore = true)
    Profile toProfile(ProfileInput req, @Context Integer role);


    Address toAddress(ProfileInput req);


    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", ignore = true)
    CartItem toCartItem(CartItemInput cartItemInput);


    @Mapping(target = "category", ignore = true)
    @Mapping(target = "productImg", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "createdBy", expression = "java(name)")
    Product toProduct(ProductInput productInput, @Context String name);


    //UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)     // handle manually
    @Mapping(target = "productImg", ignore = true)   // handle manually
    @Mapping(target = "productId", ignore = true)   // for update
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "updatedBy", expression = "java(name)")
    void updateProductFromInput(ProductInput productInput, @MappingTarget Product product, @Context String name);


    // กรณี map กลับก็ทำได้
    // ProfileInputReq toDto(Profile profile);
}

