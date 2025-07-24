package com.example.loyalty.user_preferences.domain;

import java.util.List;

public record UserPreferencesDTO(
        List<String> visitPreferences,
        List<String> foodPreferences
) {
}
