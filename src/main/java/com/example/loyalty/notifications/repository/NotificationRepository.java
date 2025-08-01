package com.example.loyalty.notifications.repository;

import com.example.loyalty.notifications.domain.Notification;
import com.example.loyalty.notifications.domain.NotificationView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>  {


    @Query(value = """
    SELECT COUNT(*) FROM notification n
    WHERE n.restaurant_id IN (:restaurantIds)
      AND n.food_type IN (:userFoodPreferences)
      AND (n.valid_until >= CURRENT_DATE)
    AND NOT EXISTS (
        SELECT 1 FROM user_notification_status uns
        WHERE uns.user_id = :userId
        AND uns.notification_id = n.id
    )
""", nativeQuery = true)
    Integer findNumberOfUnseenNotification(
            @Param("userId") String userId,
            @Param("restaurantIds") List<Long> restaurantIds,
            @Param("userFoodPreferences") Set<String> userFoodPreferences
    );

    @Query(value = """
    SELECT * FROM notification n
    WHERE n.restaurant_id IN (:restaurantIds)
      AND n.food_type IN (:userFoodPreferences)
      AND (n.valid_until >= CURRENT_DATE)
""", nativeQuery = true)
    List<Notification> findAllByRestaurantIdsAndFoodPreferences(@Param("restaurantIds") List<Long> restaurantIds,@Param("userFoodPreferences") Set<String> userFoodPreferences);

    @Query("""
    SELECT new com.example.loyalty.notifications.domain.NotificationView(
        n.restaurant.name,
        n.title,
        "",
        n.foodType,
        n.partOfDay,
        n.validFrom,
        n.validUntil
    )
    FROM Notification n
    WHERE n.restaurant.id = :restaurantId
""")
    List<NotificationView> findAllNotificationsByRestaurant(@Param("restaurantId") Long restaurantId);


}
