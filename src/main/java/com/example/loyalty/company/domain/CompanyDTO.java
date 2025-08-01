package com.example.loyalty.company.domain;

import jakarta.validation.constraints.NotBlank;

public record CompanyDTO(
        @NotBlank(message = "Name is required") String name
) {
}