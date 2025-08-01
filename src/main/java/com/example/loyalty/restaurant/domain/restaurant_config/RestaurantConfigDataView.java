package com.example.loyalty.restaurant.domain.restaurant_config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private String restaurantDisplayName;
    private String description;
    private Long premiumCouponLimit;
    private Long vipCouponLimit;
    private List<ChallengeTemplateView> challengeList;

    public RestaurantConfigDataView(
            Long id,
            String fontColor,
            String backgroundColor,
            String headerAndButtonColor,
            String logo,
            String backgroundImage,
            String restaurantDisplayName,
            String description,
            Long premiumCouponLimit,
            Long vipCouponLimit
    ) {
        this.id = id;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
        this.headerAndButtonColor = headerAndButtonColor;
        this.logo = logo;
        this.backgroundImage = backgroundImage;
        this.restaurantDisplayName = restaurantDisplayName;
        this.description = description;
        this.premiumCouponLimit = premiumCouponLimit;
        this.vipCouponLimit = vipCouponLimit;
    }

}
