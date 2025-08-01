package com.example.loyalty.dashboard.service;

import com.example.loyalty.dashboard.domain.Dashboard;
import com.example.loyalty.dashboard.domain.ReceiptCountByDay;
import com.example.loyalty.dashboard.security.DashboardRolePermissionChecker;
import com.example.loyalty.receipt.repository.ReceiptRepository;
import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.repository.RestaurantRepository;
import com.example.loyalty.user_loyalty.domain.UserLoyalty;
import com.example.loyalty.user_loyalty.repository.UserLoyaltyRepository;
import com.example.loyalty.user_preferences.domain.UserFoodPreferences;
import com.example.loyalty.user_preferences.repository.UserFoodPreferencesRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class DashboardServiceImpl implements DashboardService {

    private static final long DEFAULT_PAST_DAYS = 10;

    private final RestaurantRepository restaurantRepository;
    private final UserLoyaltyRepository userLoyaltyRepository;
    private final UserFoodPreferencesRepository userFoodPreferencesRepository;
    private final ReceiptRepository receiptRepository;
    private final DashboardRolePermissionChecker rolePermissionsChecker;

    @Override
    @Transactional(readOnly = true)
    public Dashboard findDashboardStatisticsForRestaurant(Long restaurantId, Principal principal) {
        rolePermissionsChecker.canViewAllRestaurantsStatistics(principal);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id " + restaurantId));

        List<UserLoyalty> userLoyaltyList = userLoyaltyRepository.findByRestaurantId(restaurantId);

        Map<String, Long> foodPreferencesMap = getFoodPreferencesMap(userLoyaltyList);

        Map<LocalDate, Long> dailyCounts = getReceiptPerDay(restaurantId,DEFAULT_PAST_DAYS);

        return Dashboard.builder()
                .restaurantName(restaurant.getName())
                .pib(restaurant.getPib())
                .address(restaurant.getAddress())
                .phone(restaurant.getPhone())
                .loyaltyUsers((long) userLoyaltyList.size())
                .foodPreferencesMap(foodPreferencesMap)
                .receiptsPerDay(dailyCounts)
                .build();
    }

    private Map<LocalDate, Long> getReceiptPerDay(Long restaurantId,Long pastDays) {
        LocalDate today = LocalDate.now();
        LocalDateTime startDate = today.minusDays(pastDays - 1).atStartOfDay();

        List<ReceiptCountByDay> result = receiptRepository.countReceiptsByDayForRestaurant(restaurantId, startDate);

        Map<LocalDate, Long> dailyCounts = new TreeMap<>();
        for (int i = 0; i < pastDays; i++) {
            dailyCounts.put(today.minusDays(pastDays - 1 - i), 0L);
        }

        for (ReceiptCountByDay row : result) {
            LocalDate date = row.getReceiptDate().toLocalDate();
            Long count = row.getTotal();
            dailyCounts.put(date, count);
        }

        return dailyCounts;
    }

    private Map<String, Long> getFoodPreferencesMap(List<UserLoyalty> userLoyaltyList) {
        List<String> userIds = userLoyaltyList.stream()
                .map(UserLoyalty::getUserId)
                .toList();

        List<UserFoodPreferences> allFoodPreferences = userFoodPreferencesRepository.findByUserIdIn(userIds);

        return allFoodPreferences.stream()
                .collect(Collectors.groupingBy(
                        UserFoodPreferences::getPreference,
                        Collectors.counting()
                ));
    }
}
