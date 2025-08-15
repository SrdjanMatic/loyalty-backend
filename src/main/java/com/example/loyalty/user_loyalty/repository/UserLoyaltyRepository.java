package com.example.loyalty.user_loyalty.repository;

import com.example.loyalty.user_loyalty.domain.LoyaltyId;
import com.example.loyalty.user_loyalty.domain.UserLoyalty;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserLoyaltyRepository extends JpaRepository<UserLoyalty, LoyaltyId> {
    @Query("SELECT ul.restaurantId FROM UserLoyalty ul WHERE ul.userId = :userId")
    List<Long> findRestaurantIdsByUserId(@Param("userId") String userId);

    UserLoyalty findByRestaurantIdAndUserId(Long id, String userId);

    List<UserLoyalty> findByRestaurantId(Long id);

    Collection<UserLoyalty> findAllByUserId(String userId);
}