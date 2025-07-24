package com.example.loyalty.restaurant.domain;

import java.math.BigDecimal;

public record VipRestaurantDTO(
        BigDecimal discount,
        Long restaurantId,
        String backgroundImage
) {
}
