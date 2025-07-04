package com.example.loyalty.userLoyalty.service;

import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.userLoyalty.domain.UserLoyalty;
import com.example.loyalty.userLoyalty.domain.UserLoyaltyDTO;

import java.util.List;
import java.util.Optional;

public interface UserLoyaltyService {
    void createUserLoyalty(UserLoyaltyDTO userLoyalty, String name );

}
