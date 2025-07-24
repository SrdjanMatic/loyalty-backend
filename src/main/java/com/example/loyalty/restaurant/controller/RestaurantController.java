package com.example.loyalty.restaurant.controller;

import com.example.loyalty.restaurant.domain.*;
import com.example.loyalty.restaurant.domain.restaurant_config.RestaurantConfigDataView;
import com.example.loyalty.restaurant.service.RestaurantServiceImpl;
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
    public ResponseEntity<Restaurant> create(@RequestBody RestaurantDTO restaurantDTO, Principal principal) {
        return ResponseEntity.ok(service.createRestaurant(restaurantDTO, principal));
    }

    @PostMapping("/restaurant-admin")
    public RestaurantAdminView createRestaurantAdmin(@RequestBody RestaurantAdminDTO restaurantAdminDTO, Principal principal) {
        return service.createRestaurantAdmin(restaurantAdminDTO, principal);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantWithUserLoyaltyView>> getAll(Principal principal) {
        return ResponseEntity.ok(service.getAllRestaurants(principal));
    }


    @GetMapping("/restaurant-admins")
    public ResponseEntity<Set<RestaurantAdminView>> getRestaurantAdmins(Principal principal) {
        return ResponseEntity.ok(service.getAllRestaurantAdmins(principal));
    }

    @GetMapping("/getCouponLevel/{restaurantId}")
    public ResponseEntity<RestaurantCouponLevelView> getCouponLevel(@PathVariable Long restaurantId, Principal principal) {
        return ResponseEntity.ok(service.getCouponLevel(restaurantId, principal));
    }

    @GetMapping("/config-data/{restaurantId}")
    public ResponseEntity<RestaurantConfigDataView> getRestaurantConfigData(@PathVariable Long restaurantId, Principal principal) {
        return ResponseEntity.ok(service.getRestaurantConfigData(restaurantId, principal));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getById(@PathVariable Long id) {
        return service.getRestaurantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> update(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(service.updateRestaurant(id, restaurant));
    }

    @PutMapping("/updateCouponLimit/{id}")
    public ResponseEntity<Void> updateCouponLimit(@PathVariable Long id, @RequestBody RestaurantCouponLevelView restaurantCouponLevel) {
        service.updateCouponLimit(id, restaurantCouponLevel);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
