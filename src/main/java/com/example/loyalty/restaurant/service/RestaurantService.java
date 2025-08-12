package com.example.loyalty.restaurant.service;

import com.example.loyalty.restaurant.domain.*;
import com.example.loyalty.restaurant.domain.restaurant_config.RestaurantConfigDataView;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface RestaurantService {
    Restaurant create(CreateRestaurantDTO restaurant, Principal principal);

    List<Restaurant> findAllRestaurants(Principal principal);

    List<RestaurantWithUserLoyaltyView> findAllRestaurantsWithUserLoyalty(Principal principal);

    Restaurant findRestaurantById(Long id);

    void deleteRestaurant(Long id);

    RestaurantCouponLevelView findCouponLevel(Long restaurantId, Principal principal);

    void updateCouponLimit(Long id, RestaurantCouponLevelDTO restaurantCouponLevel);

    RestaurantAdminView createRestaurantAdmin(CreateRestaurantAdminDTO restaurantAdminDTO, Principal principal);

    Set<RestaurantAdminView> findAllRestaurantAdmins(Principal principal);

    RestaurantConfigDataView findRestaurantConfigData(Long restaurantId, Principal principal);

    void deleteRestaurantAdmin(String id, Principal principal);

    RestaurantAdminView updateRestaurantAdmin(String id, @Valid UpdateRestaurantAdminDTO restaurantAdminDTO, Principal principal);

    Restaurant update(Long restaurantId, @Valid UpdateRestaurantDTO restaurantDTO, Principal principal);
}
