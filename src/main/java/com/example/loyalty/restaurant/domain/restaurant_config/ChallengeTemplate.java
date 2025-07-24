package com.example.loyalty.restaurant.domain.restaurant_config;

import com.example.loyalty.restaurant.domain.Restaurant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "restaurant_config_id")
    private RestaurantConfig restaurantConfig;

    private Integer period;
    private Integer visitsRequired;
}
