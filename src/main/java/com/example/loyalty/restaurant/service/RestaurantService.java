package com.example.loyalty.restaurant.service;

import com.example.loyalty.restaurant.domain.*;
import com.example.loyalty.restaurant.domain.restaurant_config.RestaurantConfigDataView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RestaurantService {
    Restaurant createRestaurant(RestaurantDTO restaurant, Principal principal);

    List<RestaurantWithUserLoyaltyView> getAllRestaurants(Principal principal);

    Optional<Restaurant> getRestaurantById(Long id);

    Restaurant updateRestaurant(Long id, Restaurant updatedRestaurant);

    void deleteRestaurant(Long id);

    RestaurantCouponLevelView getCouponLevel(Long restaurantId, Principal principal);

    void updateCouponLimit(Long id, RestaurantCouponLevelView restaurantCouponLevel);

    RestaurantAdminView createRestaurantAdmin(RestaurantAdminDTO restaurantAdminDTO, Principal principal);

    Set<RestaurantAdminView> getAllRestaurantAdmins(Principal principal);

    RestaurantConfigDataView getRestaurantConfigData(Long restaurantId, Principal principal);
}
