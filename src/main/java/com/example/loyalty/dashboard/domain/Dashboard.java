package com.example.loyalty.dashboard.domain;

import com.example.loyalty.user_loyalty.domain.UserLoyalty;
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
public class Dashboard {
    private String restaurantName;
    private String pib;
    private String address;
    private String phone;
    private Long loyaltyUsers;
    private Map<String, Long> foodPreferencesMap;
    private Map<LocalDate, Long> receiptsPerDay;
}
