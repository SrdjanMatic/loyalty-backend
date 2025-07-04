package com.example.loyalty.restaurant.service;

import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.domain.RestaurantDTO;
import com.example.loyalty.restaurant.domain.RestaurantWithUserLoyaltyView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface RestaurantService {
    Restaurant createRestaurant(RestaurantDTO restaurant, Principal principal);

    List<RestaurantWithUserLoyaltyView> getAllRestaurants(Principal principal);

    Optional<Restaurant> getRestaurantById(Long id);

    Restaurant updateRestaurant(Long id, Restaurant updatedRestaurant);

    void deleteRestaurant(Long id);
}
