package com.example.loyalty.restaurant.controller;

import com.example.loyalty.restaurant.domain.restaurant_config.ChallengeTemplateUserView;
import com.example.loyalty.restaurant.domain.restaurant_config.ChallengeTemplateView;
import com.example.loyalty.restaurant.domain.restaurant_config.RestaurantConfig;
import com.example.loyalty.restaurant.domain.restaurant_config.RestaurantConfigDTO;
import com.example.loyalty.restaurant.service.restaurant_config.RestaurantConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/restaurant-configs")
@RequiredArgsConstructor
public class RestaurantConfigController {

    private final RestaurantConfigService service;

    @PostMapping
    public RestaurantConfig create(@RequestBody @Valid RestaurantConfigDTO restaurantConfigDTO, Principal principal) {
        return service.createRestaurantConfig(restaurantConfigDTO, principal);
    }

    @GetMapping("/restaurant-challenge/{restaurantId}")
    public List<ChallengeTemplateView> getRestaurantChallenge(@PathVariable Long restaurantId, Principal principal) {
        return service.findRestaurantChallenge(restaurantId, principal);
    }

    @GetMapping("/restaurant-challenge/{restaurantId}/user")
    public List<ChallengeTemplateUserView> getRestaurantChallengeForUser(@PathVariable Long restaurantId, Principal principal) {
        return service.findRestaurantChallengeForUser(restaurantId, principal);
    }
}
