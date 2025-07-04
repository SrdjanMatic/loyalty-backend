package com.example.loyalty.coupon.service;

import com.example.loyalty.coupon.domain.Coupon;
import com.example.loyalty.coupon.domain.CouponDTO;
import com.example.loyalty.coupon.repository.CouponRepository;
import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    private final RestaurantRepository restaurantRepository;


    @Override
    public List<Coupon> getAllByRestaurantId(Long restaurantId) {
        return couponRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public Coupon create(CouponDTO couponDto) {

        Coupon coupon = buildCoupon(couponDto);
        return couponRepository.save(coupon);
    }

    private Coupon buildCoupon(CouponDTO couponDto) {
        Restaurant restaurant = restaurantRepository.findById(couponDto.restaurantId()).orElseThrow(() -> new EntityNotFoundException("Restaurant not found " + couponDto.restaurantId()));
        return Coupon.builder()
                .name(couponDto.name())
                .description(couponDto.description())
                .points(couponDto.points())
                .restaurant(restaurant)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
