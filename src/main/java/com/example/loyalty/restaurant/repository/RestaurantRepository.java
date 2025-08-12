package com.example.loyalty.restaurant.repository;

import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.domain.RestaurantWithUserLoyaltyView;
import com.example.loyalty.restaurant.domain.restaurant_config.RestaurantConfigDataView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("""
                SELECT new com.example.loyalty.restaurant.domain.RestaurantWithUserLoyaltyView(
                    r.id, r.name, r.address, r.phone,r.pib,
                    ul.availablePoints,ul.totalPoints, ul.joinedAt, ul.active
                )
                FROM Restaurant r
                 JOIN UserLoyalty ul ON r.id = ul.restaurantId AND ul.userId = :userId
                ORDER BY r.name
            """)
    List<RestaurantWithUserLoyaltyView> findAllWithUserLoyalty(@Param("userId") String userId);


    @Query("""
                SELECT new com.example.loyalty.restaurant.domain.restaurant_config.RestaurantConfigDataView(
                    r.id,
                    rc.fontColor,
                    rc.backgroundColor,
                    rc.headerAndButtonColor,
                    rc.logo,
                    rc.backgroundImage,
                    rc.restaurantDisplayName,
                    rc.description,
                    r.premiumCouponLimit,
                    r.vipCouponLimit
                )
                FROM Restaurant r
                JOIN RestaurantConfig rc ON r.id = rc.restaurant.id
                WHERE r.id = :restaurantId
            """)
    Optional<RestaurantConfigDataView> findConfigDataRestaurant(@Param("restaurantId") Long restaurantId);


    Optional<Restaurant> findFirstByPib(String pib);

    Optional<Restaurant> findByAdminKeycloakId(String id);
}