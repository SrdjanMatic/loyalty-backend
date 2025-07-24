package com.example.loyalty.notifications.repository;

import com.example.loyalty.notifications.domain.UserNotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationStatusRepository extends JpaRepository<UserNotificationStatus, Long> {

    @Modifying
    @Query(value = """
        INSERT INTO user_notification_status (user_id, notification_id, status, seen_at)
        VALUES (:userId, :notificationId, 'SEEN', NOW())
        ON CONFLICT (user_id, notification_id) DO NOTHING
    """, nativeQuery = true)
    void insertOneIfNotSeen(@Param("userId") String userId, @Param("notificationId") Long notificationId);
}
