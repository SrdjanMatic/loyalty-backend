package com.example.loyalty.restaurant.controller;

import com.example.loyalty.restaurant.domain.restaurant_config.ChallengeTemplateUserView;
import com.example.loyalty.restaurant.domain.restaurant_config.ChallengeTemplateView;
import com.example.loyalty.restaurant.domain.restaurant_config.RestaurantConfig;
import com.example.loyalty.restaurant.domain.restaurant_config.RestaurantConfigDTO;
import com.example.loyalty.restaurant.service.restaurant_config.RestaurantConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/restaurant-config")
@RequiredArgsConstructor
public class RestaurantConfigController {

    private final RestaurantConfigService service;

    @PostMapping
    public ResponseEntity<RestaurantConfig> create(@RequestBody RestaurantConfigDTO restaurantConfigDTO, Principal principal) {
        return ResponseEntity.ok(service.createRestaurantConfig(restaurantConfigDTO, principal));
    }

    @GetMapping("/restaurant-challenge/{restaurantId}")
    public ResponseEntity<List<ChallengeTemplateView>> getRestaurantChallenge(@PathVariable Long restaurantId, Principal principal) {
        return ResponseEntity.ok(service.getRestaurantChallenge(restaurantId, principal));
    }

    @GetMapping("/restaurant-challenge/{restaurantId}/forUser")
    public ResponseEntity<List<ChallengeTemplateUserView>> getRestaurantChallengeForUser(@PathVariable Long restaurantId, Principal principal) {
        return ResponseEntity.ok(service.getRestaurantChallengeForUser(restaurantId, principal));
    }
}
