package com.example.loyalty.user_loyalty.controller;

import com.example.loyalty.user_loyalty.domain.UserLoyalty;
import com.example.loyalty.user_loyalty.domain.UserLoyaltyDTO;
import com.example.loyalty.user_loyalty.service.UserLoyaltyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

    @GetMapping("/{restaurantId}")
    public ResponseEntity<UserLoyalty> getUserLoyaltyView(@PathVariable Long restaurantId, Principal principal) {
        return ResponseEntity.ok(service.getUserLoyaltyView(restaurantId,principal.getName()));
    }

    @PostMapping("/promote-user/{restaurantId}")
    public ResponseEntity<UserLoyalty> promoteUser(@PathVariable Long restaurantId, Principal principal) {
        return ResponseEntity.ok(service.promoteUser(restaurantId,principal.getName()));
    }

}
