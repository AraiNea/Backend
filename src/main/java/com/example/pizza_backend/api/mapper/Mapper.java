package com.example.pizza_backend.api.mapper;
import com.example.pizza_backend.api.dto.*;
import com.example.pizza_backend.api.dto.input.*;
import com.example.pizza_backend.persistence.entity.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    // Mapping จาก DTO → Entity

    // CREATE
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "profileRole", expression = "java(role)")
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "profileId", ignore = true)
    Profile toProfile(ProfileInput req, @Context Integer role);

    @Mapping(target = "addressId", ignore = true)
    Address toAddress(ProfileInput req);

    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "cartItemId", ignore = true)
    CartItem toCartItem(CartItemInput cartItemInput);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "productImg", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdBy", expression = "java(name)")
    @Mapping(target = "productId", ignore = true)
    Product toProduct(ProductInput productInput, @Context String name);

    @Mapping(target = "categoryImg", ignore = true)
    @Mapping(target = "categoryId", ignore = true)
    Category toCategory(CategoryInput categoryInput, @Context String name);

    @Mapping(target = "recommendedId", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "recommendedImg", ignore = true)
    RecommendedProduct toRecommendedProduct(RecommendedInput recommendedInput);


    //UPDATE

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "cartItemId", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "cart", ignore = true)
    void updateCartItemFromInput(CartItemInput cartItemInput, @MappingTarget CartItem cartItem);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)     // handle manually
    @Mapping(target = "productImg", ignore = true)   // handle manually
    @Mapping(target = "productId", ignore = true)   // for update
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedBy", expression = "java(name)")
    void updateProductFromInput(ProductInput productInput, @MappingTarget Product product, @Context String name);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "categoryImg", ignore = true)
    void updateCategoryFromInput(CategoryInput categoryInput, @MappingTarget Category category, @Context String name);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "addressId", ignore = true)
    void updateAddressFromInput(AddressInput addressInput, @MappingTarget Address address);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(target = "recommendedId", ignore = true)
//    @Mapping(target = "product", ignore = true)
//    @Mapping(target = "recommendedImg", ignore = true)
//    void updateRecommendedProductFromInput(RecommendedInput recommendedInput,@MappingTarget RecommendedProduct recommendedProduct);


    // กรณี map กลับส่งไปหน้าบ้าน
    AddressDto toAddressDto(Address address);

    @Mapping(target = "cartItems", ignore = true)
    CartDto toCartDto(Cart cart);

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.productDetail", target = "productDetail")
    @Mapping(source = "product.productPrice", target = "productPrice")
    CartItemDto toCartItemDto(CartItem cartItem);

    CategoryDto toCategoryDto(Category category);

    OrderItemDto toOrderItemDto(OrderItem orderItem);

    @Mapping(source = "profile.username", target = "username")
    OrderDto toOrderDto(Orders order);

    @Mapping(source = "category.categoryId", target = "categoryId")
    @Mapping(source = "category.categoryName", target = "categoryName")
    ProductDto toProductDto(Product product);

    @Mapping(source = "product.productId", target = "productId")
    RecommendedProductDto toRecommendedProductDto(RecommendedProduct recommendedProduct);
}

