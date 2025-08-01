package com.example.loyalty.restaurant.domain.restaurant_config;

import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.List;

public record RestaurantConfigDTO(
        @NotNull
        Long restaurantId,
        List<ChallengeTemplateDTO> challengeList,
        String fontColor,
        String backgroundColor,
        String headerAndButtonColor,
        @NotBlank(message = "Restaurant name can not be empty")
        String restaurantDisplayName,
        String description,
        String logo,
        String backgroundImage
) {
}
