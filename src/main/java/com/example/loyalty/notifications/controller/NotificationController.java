package com.example.loyalty.notifications.controller;

import com.example.loyalty.notifications.domain.Notification;
import com.example.loyalty.notifications.domain.NotificationDTO;
import com.example.loyalty.notifications.domain.NotificationView;
import com.example.loyalty.notifications.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PostMapping
    public Notification create(@RequestBody @Valid NotificationDTO notificationDTO, Principal principal) {
        return service.create(notificationDTO,principal);
    }

    @GetMapping("/unseen-count")
    public Integer getUnseenCount(Principal principal) {
        return service.findNumberOfUnseenNotification(principal);
    }

    @GetMapping("/user")
    public List<NotificationView> getAllNotificationsByUser(Principal principal) {
        return service.findAllNotificationsByUserAndMarkAllOfThemAsSeen(principal);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<NotificationView> getAllNotificationsByRestaurant(@PathVariable Long restaurantId, Principal principal) {
        return service.findAllNotificationsByRestaurant(restaurantId,principal);
    }
}
