package com.example.loyalty.userLoyalty.service;

import com.example.loyalty.userLoyalty.domain.UserLoyalty;
import com.example.loyalty.userLoyalty.domain.UserLoyaltyDTO;
import com.example.loyalty.userLoyalty.repository.UserLoyaltyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UserLoyaltyServiceImpl implements UserLoyaltyService {
    private final UserLoyaltyRepository userLoyaltyRepository;

    public void createUserLoyalty(UserLoyaltyDTO userLoyaltyDTO, String name) {
        UserLoyalty userLoyalty = UserLoyalty.builder()
                .userId(name)
                .restaurantId(userLoyaltyDTO.getRestaurantId())
                .joinedAt(LocalDateTime.now())
                .points(0)
                .active(true)
                .build();
        userLoyaltyRepository.save(userLoyalty);
    }

}
