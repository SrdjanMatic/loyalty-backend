package com.example.loyalty.user_loyalty.domain;

import jakarta.validation.constraints.NotNull;

public record UserLoyaltyDTO(
        @NotNull
        Long restaurantId
) {
}
