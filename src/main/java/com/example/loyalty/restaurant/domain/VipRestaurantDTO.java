package com.example.loyalty.restaurant.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record VipRestaurantDTO(
        @Min(1)
        @Max(40)
        BigDecimal discount,
        @Min(1)
        @Max(40)
        BigDecimal standardDiscount,
        @Min(1)
        @Max(40)
        BigDecimal vipDiscount,
        @Min(1)
        @Max(40)
        BigDecimal premiumDiscount,
        @NotNull
        Long restaurantId,
        String backgroundImage
) {
}
