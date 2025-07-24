package com.example.loyalty.notifications.service;


import com.example.loyalty.notifications.domain.Notification;
import com.example.loyalty.notifications.domain.NotificationDTO;
import com.example.loyalty.notifications.domain.NotificationView;
import com.example.loyalty.notifications.repository.NotificationRepository;
import com.example.loyalty.notifications.repository.UserNotificationStatusRepository;
import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.repository.RestaurantRepository;
import com.example.loyalty.user_loyalty.repository.UserLoyaltyRepository;
import com.example.loyalty.user_preferences.domain.UserFoodPreferences;
import com.example.loyalty.user_preferences.repository.UserFoodPreferencesRepository;
import com.example.loyalty.user_preferences.repository.UserVisitPreferencesRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserLoyaltyRepository userLoyaltyRepository;
    private final UserNotificationStatusRepository userNotificationStatusRepository;
    private final UserFoodPreferencesRepository userFoodPreferencesRepository;
    private final UserVisitPreferencesRepository userVisitPreferencesRepository;

    @Override
    @Transactional
    public Notification create(NotificationDTO notificationDTO, Principal principal) {

        Notification notification = buildNotification(notificationDTO);
        return notificationRepository.save(notification);
    }

    @Override
    public Integer getNumberOfUnseenNotification(Principal principal) {
        List<Long> restaurantIdsByUserId = userLoyaltyRepository.findRestaurantIdsByUserId(principal.getName());

        Set<String> userFoodPreferences = userFoodPreferencesRepository.findByUserId(principal.getName())
                .stream()
                .map(UserFoodPreferences::getPreference)
                .collect(Collectors.toSet());
        return notificationRepository.getNumberOfUnseenNotification(principal.getName(), restaurantIdsByUserId,userFoodPreferences);
    }

    @Override
    public List<NotificationView> getAllNotificationsByRestaurant(Long restaurantId, Principal principal) {
        return notificationRepository.findAllNotificationViewsByRestaurant(restaurantId);
    }

    @Override
    @Transactional
    public List<NotificationView> getAllNotificationsByUserAndMarkAllOfThemAsSeen(Principal principal) {
        List<Long> restaurantIdsByUserId = userLoyaltyRepository.findRestaurantIdsByUserId(principal.getName());
        Set<String> userFoodPreferences = userFoodPreferencesRepository.findByUserId(principal.getName())
                .stream()
                .map(UserFoodPreferences::getPreference)
                .collect(Collectors.toSet());
        List<Notification> allNotificationForUserLoyaltyRestaurants = notificationRepository.findAllByRestaurantIdsAndFoodPreferences(restaurantIdsByUserId,userFoodPreferences);

        for (Notification notification : allNotificationForUserLoyaltyRestaurants) {
            userNotificationStatusRepository.insertOneIfNotSeen(principal.getName(), notification.getId());
        }


        Set<String> userVisitPreferences = userVisitPreferencesRepository.findByUserId(principal.getName())
                .stream()
                .map(userVisitPreference -> userVisitPreference.getDayOfWeek() + "-" + userVisitPreference.getPartOfDay())
                .collect(Collectors.toSet());

        return allNotificationForUserLoyaltyRestaurants.stream()
                .map(notification -> mapToNotificationView(notification, userFoodPreferences, userVisitPreferences))
                .filter(notificationView -> !notificationView.getType().equals("no_match") && !notificationView.getType().equals("unknown"))
                .toList();
    }


    private Notification buildNotification(NotificationDTO notificationDTO) {
        Restaurant restaurant = restaurantRepository.findById(notificationDTO.restaurantId()).orElseThrow(() -> new EntityNotFoundException("Restaurant not found " + notificationDTO.restaurantId()));
        return Notification.builder()
                .title(notificationDTO.title())
                .foodType(notificationDTO.foodType())
                .partOfDay(notificationDTO.partOfDay())
                .validUntil(notificationDTO.validUntil())
                .validFrom(notificationDTO.validFrom())
                .restaurant(restaurant)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private NotificationView mapToNotificationView(Notification notification, Set<String> userFoodPreferences, Set<String> userVisitPreferences) {
        String notificationType = getNotificationType(notification, userFoodPreferences, userVisitPreferences);
        return NotificationView.builder()
                .title(notification.getTitle())
                .restaurantName(notification.getRestaurant().getName())
                .validFrom(notification.getValidFrom())
                .type(notificationType)
                .validUntil(notification.getValidUntil())
                .foodType(notification.getFoodType())
                .partOfDay(notification.getPartOfDay())
                .build();
    }

    private static String getNotificationType(Notification notification, Set<String> userFoodPreferences, Set<String> userVisitPreferences) {
        if (notification == null || userFoodPreferences == null || userVisitPreferences == null) {
            return "unknown";
        }

        String foodType = notification.getFoodType();
        String partOfDay = notification.getPartOfDay();
        String dayOfWeek = notification.getValidFrom().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        ;

        String visitKey = dayOfWeek + "-" + partOfDay;

        boolean matchesFood = userFoodPreferences.contains(foodType);
        boolean matchesVisit = userVisitPreferences.contains(visitKey);

        // Should be enum also in DB
        if (matchesFood && matchesVisit) {
            return "perfect_match";
        } else if (matchesFood) {
            return "food_match";
        } else {
            return "no_match";
        }
    }
}
