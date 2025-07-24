package com.example.loyalty.coupon.domain;

public record CouponDTO(
        String name,
        Integer points,
        Long restaurantId,
        String description,
        Coupon.Level level
) {
}
