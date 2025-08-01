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
public class RestaurantConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private String fontColor;
    private String backgroundColor;
    private String headerAndButtonColor;
    private String logo;
    private String backgroundImage;
    private String restaurantDisplayName;
    private String description;

}
