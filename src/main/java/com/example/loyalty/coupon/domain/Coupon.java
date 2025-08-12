package com.example.loyalty.coupon.domain;

import com.example.loyalty.base.domain.AbstractBaseEntity;
import com.example.loyalty.restaurant.domain.Restaurant;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Coupon extends AbstractBaseEntity {

    public enum Level {
        STANDARD,
        PREMIUM,
        VIP
    }

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Level level;


    @OneToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private Integer points;
}
