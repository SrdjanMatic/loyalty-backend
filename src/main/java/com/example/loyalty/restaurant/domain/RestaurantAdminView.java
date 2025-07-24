package com.example.loyalty.restaurant.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantAdminView {
    private String keycloakId;
    private String restaurantName;
    private String firstName;
    private String username;
    private String lastName;
    private String email;
}
