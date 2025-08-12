package com.example.loyalty.coupon.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CouponDTO(

        @NotBlank(message = "Name is required")
        String name,

        @NotNull @Min(1)
        Integer points,

        @NotNull
        Long restaurantId,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull
        Coupon.Level level
) {
}
