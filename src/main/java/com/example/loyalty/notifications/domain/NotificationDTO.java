package com.example.loyalty.notifications.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record NotificationDTO(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Food type is required")
        String foodType,

        @NotBlank(message = "Part of day is required")
        String partOfDay,

        @NotNull
        Long restaurantId,

        @NotNull
        LocalDate validUntil,

        @NotNull
        LocalDate validFrom
) {
}
