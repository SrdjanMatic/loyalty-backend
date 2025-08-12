package com.example.loyalty.restaurant.domain;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

public record CreateRestaurantDTO(
        @NotBlank(message = "Name can not be empty")
        String name,
        @NotBlank(message = "Address can not be empty")
        String address,
        @NotBlank(message = "Pib can not be empty")
        String pib,
        String phone,
        @NotBlank(message = "Restaurant admin can not be empty")
        String adminKeycloakId
) {
}