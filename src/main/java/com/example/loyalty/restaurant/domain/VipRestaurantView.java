package com.example.loyalty.restaurant.domain;

import com.example.loyalty.coupon.domain.Coupon;

import java.math.BigDecimal;
import java.util.Map;

public interface VipRestaurantView {
    Long getId();
    RestaurantView getRestaurant();
    BigDecimal getGeneralDiscount();
    String getBackgroundImage();
    Map<Coupon.Level, BigDecimal> getLevelDiscounts();

    interface RestaurantView {
        Long getId();
        String getName();
    }
}
