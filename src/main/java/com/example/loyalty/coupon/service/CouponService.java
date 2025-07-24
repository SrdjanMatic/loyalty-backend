package com.example.loyalty.coupon.service;

import com.example.loyalty.coupon.domain.Coupon;
import com.example.loyalty.coupon.domain.CouponDTO;

import java.security.Principal;
import java.util.List;

public interface CouponService {
    List<Coupon> getAllByRestaurantId(Long restaurantId);

    Coupon create(CouponDTO coupon);

    List<Coupon> getAllByRestaurantIdAndCouponLevel(Long restaurantId, Principal principal);
}
