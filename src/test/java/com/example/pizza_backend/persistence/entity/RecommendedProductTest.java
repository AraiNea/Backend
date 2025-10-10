package com.example.pizza_backend.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecommendedProductTest {

    @Test
    void testBuilderAndGetters() {
        Product product = Product.builder().productId(1L).productName("Pizza").build();

        RecommendedProduct rp = RecommendedProduct.builder()
                .recommendedImg("rec1.png")
                .product(product)
                .build();

        assertThat(rp.getProduct()).isEqualTo(product);
        assertThat(rp.getRecommendedImg()).isEqualTo("rec1.png");
        assertThat(rp.getRecommendImgPath()).isEqualTo("/Images/recommended-photos/rec1.png");
    }

    @Test
    void testSetterAndGetters() {
        RecommendedProduct rp = new RecommendedProduct();
        Product product = Product.builder().productId(2L).productName("Burger").build();

        rp.setProduct(product);
        rp.setRecommendedImg("rec2.png");

        assertThat(rp.getProduct()).isEqualTo(product);
        assertThat(rp.getRecommendedImg()).isEqualTo("rec2.png");
        assertThat(rp.getRecommendImgPath()).isEqualTo("/Images/recommended-photos/rec2.png");

        rp.setRecommendedImg(null);
        assertThat(rp.getRecommendImgPath()).isNull();
    }

    @Test
    void testEqualsAndHashCode() {
        RecommendedProduct rp1 = RecommendedProduct.builder().recommendedImg("rec1.png").build();
        RecommendedProduct rp2 = RecommendedProduct.builder().recommendedImg("rec1.png").build();

        assertThat(rp1).isEqualTo(rp2);
        assertThat(rp1.hashCode()).isEqualTo(rp2.hashCode());
    }

    @Test
    void testToString() {
        RecommendedProduct rp = RecommendedProduct.builder()
                .recommendedImg("rec1.png")
                .build();

        String str = rp.toString();
        assertThat(str).contains("recommendedImg=rec1.png");
    }

    @Test
    void testRecommendImgPath() {
        RecommendedProduct rp = new RecommendedProduct();
        rp.setRecommendedImg("img.png");
        assertThat(rp.getRecommendImgPath()).isEqualTo("/Images/recommended-photos/img.png");

        rp.setRecommendedImg(null);
        assertThat(rp.getRecommendImgPath()).isNull();
    }
}
