package com.example.loyalty.receipt.domain;

public record GameDTO (
        Long challengeId,
        String receiptKey,
        Long gamePoints
) {
}
