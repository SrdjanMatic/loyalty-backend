package com.example.loyalty.notifications.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NotificationView {
    private String restaurantName;
    private String title;
    private String type;
    private String foodType;
    private String partOfDay;
    private LocalDate validFrom;
    private LocalDate validUntil;

}
