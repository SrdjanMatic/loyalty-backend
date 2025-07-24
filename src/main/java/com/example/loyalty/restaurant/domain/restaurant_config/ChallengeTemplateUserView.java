package com.example.loyalty.restaurant.domain.restaurant_config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeTemplateUserView {
    private Long id;
    private Long period;
    private Long visitsRequired;
    private Long visitsCompleted;
}
