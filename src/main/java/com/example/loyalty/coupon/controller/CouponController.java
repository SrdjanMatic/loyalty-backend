package com.example.loyalty.coupon.controller;

import com.example.loyalty.coupon.domain.Coupon;
import com.example.loyalty.coupon.domain.CouponDTO;
import com.example.loyalty.coupon.service.CouponServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponServiceImpl service;

    @PostMapping
    public Coupon create(@RequestBody @Valid CouponDTO coupon, Principal principal) {
        return service.create(coupon, principal);
    }

    @GetMapping("/{restaurantId}")
    public List<Coupon> getAllCouponsByRestaurant(@PathVariable Long restaurantId) {
        return service.findAllByRestaurantId(restaurantId);
    }

    @GetMapping("/user/{restaurantId}")
    public List<Coupon> getAllRestaurantCouponsByUser(@PathVariable Long restaurantId, Principal principal) {
        return service.findAllByRestaurantIdAndCouponLevel(restaurantId, principal);
    }
}
