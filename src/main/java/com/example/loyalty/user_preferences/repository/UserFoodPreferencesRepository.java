package com.example.loyalty.user_preferences.repository;

import com.example.loyalty.user_preferences.domain.UserFoodPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFoodPreferencesRepository extends JpaRepository<UserFoodPreferences, Long> {
    List<UserFoodPreferences> findByUserId(String userId);

    List<UserFoodPreferences> findByUserIdIn(List<String> userIds);
}