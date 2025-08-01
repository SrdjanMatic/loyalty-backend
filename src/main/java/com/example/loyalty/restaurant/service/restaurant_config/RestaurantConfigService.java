package com.example.loyalty.restaurant.service.restaurant_config;

import com.example.loyalty.restaurant.domain.restaurant_config.ChallengeTemplateUserView;
import com.example.loyalty.restaurant.domain.restaurant_config.ChallengeTemplateView;
import com.example.loyalty.restaurant.domain.restaurant_config.RestaurantConfig;
import com.example.loyalty.restaurant.domain.restaurant_config.RestaurantConfigDTO;

import java.security.Principal;
import java.util.List;

public interface RestaurantConfigService {
    RestaurantConfig createRestaurantConfig(RestaurantConfigDTO restaurantConfigDTO, Principal principal);

    List<ChallengeTemplateView> findRestaurantChallenge(Long restaurantId, Principal principal);

    List<ChallengeTemplateUserView> findRestaurantChallengeForUser(Long restaurantId, Principal principal);
}
