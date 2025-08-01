package com.example.loyalty.restaurant.domain;

import jakarta.validation.constraints.NotNull;

public record RestaurantCouponLevelDTO(
        @NotNull
        Long premiumCouponLimit,
        @NotNull
        Long vipCouponLimit
) {
}
