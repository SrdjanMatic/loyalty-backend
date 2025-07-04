package com.example.loyalty.userLoyalty.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(LoyaltyId.class)
public class UserLoyalty {

    @Id
    private String userId;

    @Id
    private Integer restaurantId;

    private Integer points = 0;

    private LocalDateTime joinedAt = LocalDateTime.now();

    private boolean active = true;
}
