package com.example.loyalty.restaurant.domain;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

public record UpdateRestaurantDTO (
        @NotBlank(message = "Name can not be empty")
        String name,
        @NotBlank(message = "Address can not be empty")
        String address,
        String phone,
        @NotBlank(message = "Restaurant admin can not be empty")
        String adminKeycloakId,
        String pib

){
}
