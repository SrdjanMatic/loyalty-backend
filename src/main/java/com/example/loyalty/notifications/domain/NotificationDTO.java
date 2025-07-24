package com.example.loyalty.notifications.domain;

import java.time.LocalDate;
import java.util.List;

public record NotificationDTO(
        String title,
        String foodType,
        String partOfDay,
        Long restaurantId,
        LocalDate validUntil,
        LocalDate validFrom
) {
}
