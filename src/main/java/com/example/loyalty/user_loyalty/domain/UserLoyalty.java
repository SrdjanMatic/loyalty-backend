package com.example.loyalty.user_loyalty.domain;

import com.example.loyalty.coupon.domain.Coupon;
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

    public enum UserLoyaltyLevel {
        STANDARD,
        PROMOTED_TO_PREMIUM,
        PREMIUM,
        PROMOTED_TO_VIP,
        VIP
    }

    @Id
    private String userId;

    @Id
    private Long restaurantId;

    private Long availablePoints;

    private Long totalPoints;

    @Enumerated(EnumType.STRING)
    private UserLoyaltyLevel level;

    private LocalDateTime joinedAt;

    private boolean active;
}
