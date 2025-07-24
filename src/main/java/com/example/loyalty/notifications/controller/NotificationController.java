package com.example.loyalty.notifications.controller;

import com.example.loyalty.notifications.domain.Notification;
import com.example.loyalty.notifications.domain.NotificationDTO;
import com.example.loyalty.notifications.domain.NotificationView;
import com.example.loyalty.notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PostMapping
    public ResponseEntity<Notification> create(@RequestBody NotificationDTO notificationDTO, Principal principal) {
        return ResponseEntity.ok(service.create(notificationDTO,principal));
    }

    @GetMapping("/getNumberOfUnseenNotifications")
    public ResponseEntity<Integer> getNumberOfUnseenNotification(Principal principal) {
        return ResponseEntity.ok(service.getNumberOfUnseenNotification(principal));
    }

    @GetMapping("/getAllNotificationsByUser")
    public ResponseEntity<List<NotificationView>> getAllNotificationsByUser(Principal principal) {
        return ResponseEntity.ok(service.getAllNotificationsByUserAndMarkAllOfThemAsSeen(principal));
    }

    @GetMapping("/getAllByRestaurant/{restaurantId}")
    public ResponseEntity<List<NotificationView>> getAllNotificationsByRestaurant(@PathVariable Long restaurantId, Principal principal) {
        return ResponseEntity.ok(service.getAllNotificationsByRestaurant(restaurantId,principal));
    }
}
