package com.example.loyalty.restaurant.domain.restaurant_config;

import java.util.List;

public record RestaurantConfigDTO(
        Long restaurantId,
        List<ChallengeTemplateDTO> challengeList,
        String fontColor,
        String backgroundColor,
        String headerAndButtonColor,
        String restaurantName,
        String description,
        String logo,
        String backgroundImage
) {
}
