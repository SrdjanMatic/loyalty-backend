package com.example.loyalty.restaurant.repository;

import com.example.loyalty.restaurant.domain.restaurant_config.ChallengeTemplate;
import com.example.loyalty.restaurant.domain.restaurant_config.ChallengeTemplateView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeTemplateRepository extends JpaRepository<ChallengeTemplate, Long> {
    List<ChallengeTemplateView> findByRestaurantConfigRestaurantId(Long restaurantId);
}
