package com.example.loyalty.notifications.service;


import com.example.loyalty.notifications.domain.Notification;
import com.example.loyalty.notifications.domain.NotificationDTO;
import com.example.loyalty.notifications.domain.NotificationView;

import java.security.Principal;
import java.util.List;

public interface NotificationService {

    Notification create(NotificationDTO notificationDTO, Principal principal);

    Integer findNumberOfUnseenNotification(Principal principal);

    List<NotificationView> findAllNotificationsByUserAndMarkAllOfThemAsSeen(Principal principal);

    List<NotificationView> findAllNotificationsByRestaurant(Long restaurantId, Principal principal);
}
