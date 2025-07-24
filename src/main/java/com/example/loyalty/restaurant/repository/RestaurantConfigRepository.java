package com.example.loyalty.restaurant.repository;

import com.example.loyalty.restaurant.domain.restaurant_config.RestaurantConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantConfigRepository extends JpaRepository<RestaurantConfig, Long> {

    Optional<RestaurantConfig> findByRestaurantId(Long restaurantId);
}
