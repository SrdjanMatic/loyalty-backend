package com.example.loyalty.coupon.domain;

public interface CouponView {
        Long getId();
        String getName();
        String getDescription();
        Integer getPoints();
        Long getRestaurantId();
        Coupon.Level getLevel();
}
