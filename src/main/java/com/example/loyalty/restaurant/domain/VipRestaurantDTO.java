package com.example.loyalty.restaurant.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record VipRestaurantDTO(
        @NotNull
        @Min(1)
        @Max(100)
        BigDecimal discount,
        @NotNull
        Long restaurantId,
        String backgroundImage
) {
}
