package com.example.loyalty.restaurant.domain.restaurant_config;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReceiptChallengeUsageId implements Serializable {
    private String receiptKey;
    private Long challengeTemplateId;
}
