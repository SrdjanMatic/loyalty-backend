package com.example.loyalty.restaurant.controller;

import com.example.loyalty.restaurant.domain.VipRestaurant;
import com.example.loyalty.restaurant.domain.VipRestaurantDTO;
import com.example.loyalty.restaurant.service.VipRestaurantServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/vip-restaurant")
@RequiredArgsConstructor
public class VipRestaurantController {

    private final VipRestaurantServiceImpl service;

    @PostMapping
    public ResponseEntity<VipRestaurant> create(@RequestBody VipRestaurantDTO dto, Principal principal) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<VipRestaurant>> getAll(Principal principal) {
        return ResponseEntity.ok(service.getAll());
    }
}
