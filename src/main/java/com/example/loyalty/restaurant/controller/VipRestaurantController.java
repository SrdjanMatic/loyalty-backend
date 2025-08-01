package com.example.loyalty.restaurant.controller;

import com.example.loyalty.restaurant.domain.VipRestaurant;
import com.example.loyalty.restaurant.domain.VipRestaurantDTO;
import com.example.loyalty.restaurant.service.vip_restaurant.VipRestaurantServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/vip-restaurants")
@RequiredArgsConstructor
public class VipRestaurantController {

    private final VipRestaurantServiceImpl service;

    @PostMapping
    public VipRestaurant create(@RequestBody @Valid VipRestaurantDTO dto, Principal principal) {
        return service.create(dto, principal);
    }

    @GetMapping
    public List<VipRestaurant> getAll(Principal principal) {
        return service.findAll();
    }
}
