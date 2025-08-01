package com.example.loyalty.receipt.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GameDTO (
        @NotNull
        Long challengeId,
        @NotBlank
        String receiptKey,
        @NotNull
        Long gamePoints
) {
}
