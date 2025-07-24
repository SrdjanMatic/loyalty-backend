package com.example.loyalty.restaurant.domain;

import java.time.LocalDateTime;

public record RestaurantWithUserLoyaltyView(
        Long id,
        String name,
        String address,
        String phone,
        String pib,
        Long availablePoints,
        Long totalPoints,
        LocalDateTime joinedAt,
        Boolean active
) {}
