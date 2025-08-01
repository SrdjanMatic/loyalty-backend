package com.example.loyalty.user_loyalty.service;

import com.example.loyalty.user_loyalty.domain.UserLoyalty;
import com.example.loyalty.user_loyalty.domain.UserLoyaltyDTO;

public interface UserLoyaltyService {
    void createUserLoyalty(UserLoyaltyDTO userLoyalty, String name );

    UserLoyalty findUserLoyaltyView(Long restaurantId, String name);

    UserLoyalty promoteUser(Long restaurantId, String name);
}
