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
    public Restaurant create(@RequestBody @Valid CreateRestaurantDTO restaurantDTO, Principal principal) {
        return service.create(restaurantDTO, principal);
    }

    @PutMapping("/{id}")
    public Restaurant update(@PathVariable Long id, @RequestBody @Valid UpdateRestaurantDTO restaurantDTO, Principal principal) {
        return service.update(id,restaurantDTO, principal);
    }

    @PostMapping("/restaurant-admin")
    public RestaurantAdminView createRestaurantAdmin(@RequestBody @Valid CreateRestaurantAdminDTO restaurantAdminDTO, Principal principal) {
        return service.createRestaurantAdmin(restaurantAdminDTO, principal);
    }

    @PutMapping("/restaurant-admin/{id}")
    public RestaurantAdminView updateRestaurantAdmin(@PathVariable String id, @RequestBody @Valid UpdateRestaurantAdminDTO restaurantAdminDTO, Principal principal) {
        return service.updateRestaurantAdmin(id, restaurantAdminDTO, principal);
    }

    @DeleteMapping("/restaurant-admin/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, Principal principal) {
        service.deleteRestaurantAdmin(id, principal);
        return ResponseEntity.noContent().build();
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
