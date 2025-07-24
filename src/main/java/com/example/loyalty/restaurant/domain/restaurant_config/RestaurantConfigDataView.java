package com.example.loyalty.restaurant.domain.restaurant_config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantConfigDataView {
    private Long id;
    private String fontColor;
    private String backgroundColor;
    private String headerAndButtonColor;
    private String logo;
    private String backgroundImage;
    private String restaurantName;
    private String description;
    private Long premiumCouponLimit;
    private Long vipCouponLimit;
}
