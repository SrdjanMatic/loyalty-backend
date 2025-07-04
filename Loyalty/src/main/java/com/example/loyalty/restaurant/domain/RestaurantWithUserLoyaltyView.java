package com.example.loyalty.restaurant.domain;

import java.time.LocalDateTime;

public record RestaurantWithUserLoyaltyView(
        Long id,
        String name,
        String address,
        String phone,
        Integer pib,
        Integer points,
        LocalDateTime joinedAt,
        Boolean active
) {}
