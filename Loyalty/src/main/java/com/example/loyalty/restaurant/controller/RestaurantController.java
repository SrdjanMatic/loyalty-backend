package com.example.loyalty.restaurant.controller;

import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.domain.RestaurantDTO;
import com.example.loyalty.restaurant.domain.RestaurantWithUserLoyaltyView;
import com.example.loyalty.restaurant.service.RestaurantServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantServiceImpl service;

    @PostMapping
    public ResponseEntity<Restaurant> create(@RequestBody RestaurantDTO restaurantDTO, Principal principal) {
        return ResponseEntity.ok(service.createRestaurant(restaurantDTO,principal));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantWithUserLoyaltyView>> getAll(Principal principal) {
        return ResponseEntity.ok(service.getAllRestaurants(principal));
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
