package com.example.loyalty.restaurant.domain;

public record RestaurantDTO(
        String name,
        String address,
        Integer pib,
        String phone
) {
}