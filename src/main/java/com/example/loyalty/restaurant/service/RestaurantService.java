package com.example.loyalty.restaurant.service;

import com.example.loyalty.restaurant.domain.*;
import com.example.loyalty.restaurant.domain.restaurant_config.RestaurantConfigDataView;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface RestaurantService {
    Restaurant createRestaurant(RestaurantDTO restaurant, Principal principal);

    List<Restaurant> findAllRestaurants(Principal principal);

    List<RestaurantWithUserLoyaltyView> findAllRestaurantsWithUserLoyalty(Principal principal);

    Restaurant findRestaurantById(Long id);

    Restaurant updateRestaurant(Long id, Restaurant updatedRestaurant);

    void deleteRestaurant(Long id);

    RestaurantCouponLevelView findCouponLevel(Long restaurantId, Principal principal);

    void updateCouponLimit(Long id, RestaurantCouponLevelDTO restaurantCouponLevel);

    RestaurantAdminView createRestaurantAdmin(RestaurantAdminDTO restaurantAdminDTO, Principal principal);

    Set<RestaurantAdminView> findAllRestaurantAdmins(Principal principal);

    RestaurantConfigDataView findRestaurantConfigData(Long restaurantId, Principal principal);
}
