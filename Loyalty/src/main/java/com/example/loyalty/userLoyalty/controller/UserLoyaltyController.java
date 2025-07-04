package com.example.loyalty.userLoyalty.controller;

import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.service.RestaurantServiceImpl;
import com.example.loyalty.userLoyalty.domain.UserLoyalty;
import com.example.loyalty.userLoyalty.domain.UserLoyaltyDTO;
import com.example.loyalty.userLoyalty.service.UserLoyaltyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/userLoyalty")
@RequiredArgsConstructor
public class UserLoyaltyController {

    private final UserLoyaltyServiceImpl service;
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody UserLoyaltyDTO userLoyalty, Principal principal) {
        service.createUserLoyalty(userLoyalty, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
