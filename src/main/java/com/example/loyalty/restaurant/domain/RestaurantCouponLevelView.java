package com.example.loyalty.restaurant.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantCouponLevelView{
    private Long premiumCouponLimit;
    private Long vipCouponLimit;
}
