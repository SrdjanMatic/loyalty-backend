package com.example.loyalty.user_loyalty.service;

import com.example.loyalty.user_loyalty.domain.UserLoyalty;
import com.example.loyalty.user_loyalty.domain.UserLoyaltyDTO;
import com.example.loyalty.user_loyalty.repository.UserLoyaltyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserLoyaltyServiceImpl implements UserLoyaltyService {
    private final UserLoyaltyRepository userLoyaltyRepository;

    public void createUserLoyalty(UserLoyaltyDTO userLoyaltyDTO, String name) {
        UserLoyalty userLoyalty = UserLoyalty.builder()
                .userId(name)
                .restaurantId(userLoyaltyDTO.restaurantId())
                .joinedAt(LocalDateTime.now())
                .availablePoints(0L)
                .level(UserLoyalty.UserLoyaltyLevel.STANDARD)
                .totalPoints(0L)
                .active(true)
                .build();
        userLoyaltyRepository.save(userLoyalty);
    }

    @Override
    public UserLoyalty findUserLoyaltyView(Long restaurantId, String name) {
        return userLoyaltyRepository.findByRestaurantIdAndUserId(restaurantId, name);
    }

    @Override
    @Transactional
    public UserLoyalty promoteUser(Long restaurantId, String userId) {
        UserLoyalty userLoyalty = userLoyaltyRepository
                .findByRestaurantIdAndUserId(restaurantId, userId);

        UserLoyalty.UserLoyaltyLevel currentLevel = userLoyalty.getLevel();

        switch (currentLevel) {
            case PROMOTED_TO_PREMIUM -> userLoyalty.setLevel(UserLoyalty.UserLoyaltyLevel.PREMIUM);
            case PROMOTED_TO_VIP -> userLoyalty.setLevel(UserLoyalty.UserLoyaltyLevel.VIP);
            default -> {
                log.warn("User {} at restaurant {} is not in a promotable level: {}", userId, restaurantId, currentLevel);
                return userLoyalty; // No change
            }
        }

        return userLoyaltyRepository.save(userLoyalty);
    }

}
