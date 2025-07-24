package com.example.loyalty.user_preferences.repository;

import com.example.loyalty.user_preferences.domain.UserVisitPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserVisitPreferencesRepository extends JpaRepository<UserVisitPreferences, Long> {
    List<UserVisitPreferences> findByUserId(String userId);
}
