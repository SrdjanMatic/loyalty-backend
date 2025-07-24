package com.example.loyalty.restaurant.service;

import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.domain.VipRestaurant;
import com.example.loyalty.restaurant.domain.VipRestaurantDTO;
import com.example.loyalty.restaurant.repository.RestaurantRepository;
import com.example.loyalty.restaurant.repository.VipRestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class VipRestaurantServiceImpl implements VipRestaurantService {

    private final VipRestaurantRepository vipRestaurantRepository;

    private final RestaurantRepository restaurantRepository;


    @Override
    public List<VipRestaurant> getAll() {
        return vipRestaurantRepository.findAll();
    }

    @Override
    public VipRestaurant create(VipRestaurantDTO vipRestaurantDTO) {

        VipRestaurant vipRestaurant = buildVipRestaurant(vipRestaurantDTO);
        return vipRestaurantRepository.save(vipRestaurant);
    }

    private VipRestaurant buildVipRestaurant(VipRestaurantDTO vipRestaurantDTO) {
        Restaurant restaurant = restaurantRepository.findById(vipRestaurantDTO.restaurantId()).orElseThrow(() -> new EntityNotFoundException("Restaurant not found " + vipRestaurantDTO.restaurantId()));
        return VipRestaurant.builder()
                .discount(vipRestaurantDTO.discount())
                .backgroundImage(vipRestaurantDTO.backgroundImage())
                .restaurant(restaurant)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
