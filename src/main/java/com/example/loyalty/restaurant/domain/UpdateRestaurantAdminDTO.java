package com.example.loyalty.restaurant.domain;

import jakarta.validation.constraints.Email;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

public record UpdateRestaurantAdminDTO(
        @Email
        @NotBlank(message = "Email can not be empty")
        String email,
        @NotBlank(message = "First name can not be empty")
        String firstName,
        @NotBlank(message = "Last name can not be empty")
        String lastName
) {
}
