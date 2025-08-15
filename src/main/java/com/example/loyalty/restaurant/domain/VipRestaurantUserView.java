package com.example.loyalty.restaurant.domain;

import com.example.loyalty.coupon.domain.Coupon;
import com.example.loyalty.user_loyalty.domain.UserLoyalty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VipRestaurantUserView {
    private Long id;
    private String restaurantName;
    private BigDecimal generalDiscount;
    private UserLoyalty.UserLoyaltyLevel level;
    private BigDecimal levelDiscount;
    private String backgroundImage;
}
