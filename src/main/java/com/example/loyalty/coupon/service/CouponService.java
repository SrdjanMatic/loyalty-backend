package com.example.loyalty.coupon.service;

import com.example.loyalty.coupon.domain.Coupon;
import com.example.loyalty.coupon.domain.CouponDTO;
import com.example.loyalty.coupon.domain.CouponView;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;

public interface CouponService {
    List<CouponView> findAllByRestaurantId(Long restaurantId);

    CouponView create(CouponDTO coupon, Principal principal);

    List<CouponView> findAllByRestaurantIdAndCouponLevel(Long restaurantId, Principal principal);

    void delete(Long id, Principal principal);

    CouponView update(Long id, @Valid CouponDTO couponDTO, Principal principal);
}
