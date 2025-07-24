package com.example.loyalty.user_loyalty.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoyaltyId implements Serializable {
    private String userId;
    private Long restaurantId;
}
