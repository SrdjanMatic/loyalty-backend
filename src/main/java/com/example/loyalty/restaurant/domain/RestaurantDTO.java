package com.example.loyalty.restaurant.domain;

public record RestaurantDTO(
        String name,
        String address,
        String pib,
        String phone,
        String restaurantAdmin
) {
}