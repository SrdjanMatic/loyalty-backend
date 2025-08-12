package com.example.loyalty.coupon.controller;

import com.example.loyalty.coupon.domain.CouponDTO;
import com.example.loyalty.coupon.domain.CouponView;
import com.example.loyalty.coupon.service.CouponServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponServiceImpl service;

    @PostMapping
    public CouponView create(@RequestBody @Valid CouponDTO coupon, Principal principal) {
        return service.create(coupon, principal);
    }

    @GetMapping
    public List<CouponView> getAllCouponsByRestaurant(@RequestParam Long restaurantId, Principal principal) {
        return service.findAllByRestaurantId(restaurantId);
    }

    @GetMapping("/user")
    public List<CouponView> getAllRestaurantCouponsByUser(@RequestParam Long restaurantId, Principal principal) {
        return service.findAllByRestaurantIdAndCouponLevel(restaurantId, principal);
    }

    @PutMapping("/{id}")
    public CouponView update(@PathVariable Long id,
                              @RequestBody @Valid CouponDTO couponDTO, Principal principal) {
        return service.update(id, couponDTO, principal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
        service.delete(id, principal);
        return ResponseEntity.noContent().build();
    }
}
