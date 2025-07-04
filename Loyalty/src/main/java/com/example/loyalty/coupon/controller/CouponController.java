package com.example.loyalty.coupon.controller;

import com.example.loyalty.coupon.domain.Coupon;
import com.example.loyalty.coupon.domain.CouponDTO;
import com.example.loyalty.coupon.service.CouponServiceImpl;
import com.example.loyalty.restaurant.domain.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final CouponServiceImpl service;

    @PostMapping
    public ResponseEntity<Coupon> create(@RequestBody CouponDTO coupon, Principal principal) {
        return ResponseEntity.ok(service.create(coupon));
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<List<Coupon>> getAll(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(service.getAllByRestaurantId(restaurantId));
    }
}
