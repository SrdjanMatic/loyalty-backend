package com.example.loyalty.restaurant.repository;

import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.domain.RestaurantWithUserLoyaltyView;
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
        ul.points, ul.joinedAt, ul.active
    )
    FROM Restaurant r
    LEFT JOIN UserLoyalty ul ON r.id = ul.restaurantId AND ul.userId = :userId
    ORDER BY r.name
""")
    List<RestaurantWithUserLoyaltyView> findAllWithUserLoyalty(@Param("userId") String userId);
    Optional<Restaurant> findFirstByPib(Integer pib);
}