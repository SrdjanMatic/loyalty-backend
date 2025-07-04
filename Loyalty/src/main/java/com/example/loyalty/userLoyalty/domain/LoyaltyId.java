package com.example.loyalty.userLoyalty.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoyaltyId implements Serializable {
    private String userId;
    private Integer restaurantId;
}
