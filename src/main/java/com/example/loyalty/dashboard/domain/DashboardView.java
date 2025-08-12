package com.example.loyalty.dashboard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardView {
    private String restaurantName;
    private String pib;
    private String address;
    private String phone;
    private Long loyaltyUsers;
    private Map<String, Long> foodPreferencesMap;
    private Map<LocalDate, Long> receiptsPerDay;
}
