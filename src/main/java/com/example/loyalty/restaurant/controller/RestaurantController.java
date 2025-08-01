package com.example.loyalty.restaurant.controller;

import com.example.loyalty.restaurant.domain.*;
import com.example.loyalty.restaurant.domain.restaurant_config.RestaurantConfigDataView;
import com.example.loyalty.restaurant.service.RestaurantServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantServiceImpl service;

    @PostMapping
    public Restaurant create(@RequestBody @Valid RestaurantDTO restaurantDTO, Principal principal) {
        return service.createRestaurant(restaurantDTO, principal);
    }

    @PostMapping("/restaurant-admin")
    public RestaurantAdminView createRestaurantAdmin(@RequestBody @Valid RestaurantAdminDTO restaurantAdminDTO, Principal principal) {
        return service.createRestaurantAdmin(restaurantAdminDTO, principal);
    }

    @GetMapping
    public List<Restaurant> getAll(Principal principal) {
        return service.findAllRestaurants(principal);
    }

    @GetMapping("/userLoyalty")
    public List<RestaurantWithUserLoyaltyView> getAllRestaurantsWithUserLoyalty(Principal principal) {
        return service.findAllRestaurantsWithUserLoyalty(principal);
    }

    @GetMapping("/restaurant-admins")
    public Set<RestaurantAdminView> getAllRestaurantAdmins(Principal principal) {
        return service.findAllRestaurantAdmins(principal);
    }

    @GetMapping("/coupon-limit/{restaurantId}")
    public RestaurantCouponLevelView getCouponLevel(@PathVariable Long restaurantId, Principal principal) {
        return service.findCouponLevel(restaurantId, principal);
    }

    @GetMapping("/config-data/{restaurantId}")
    public RestaurantConfigDataView getRestaurantConfigData(@PathVariable Long restaurantId, Principal principal) {
        return service.findRestaurantConfigData(restaurantId, principal);
    }

    @GetMapping("/{id}")
    public Restaurant getRestaurantById(@PathVariable Long id) {
        return service.findRestaurantById(id);
    }

    @PutMapping("/{id}")
    public Restaurant update(@PathVariable Long id, @RequestBody @Valid Restaurant restaurant) {
        return service.updateRestaurant(id, restaurant);
    }

    @PutMapping("/coupon-limit/{id}")
    public ResponseEntity<Void> updateCouponLimit(@PathVariable Long id, @RequestBody @Valid RestaurantCouponLevelDTO restaurantCouponLevel) {
        service.updateCouponLimit(id, restaurantCouponLevel);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
