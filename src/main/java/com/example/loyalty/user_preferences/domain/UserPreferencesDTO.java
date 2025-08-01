package com.example.loyalty.user_preferences.domain;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UserPreferencesDTO(
        @NotEmpty (message = "List of visit preferences can not be empty")
        List<String> visitPreferences,

        @NotEmpty (message = "List of food preferences can not be empty")
        List<String> foodPreferences
) {
}
