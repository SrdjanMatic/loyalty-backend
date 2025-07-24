package com.example.loyalty.coupon.service;

import com.example.loyalty.coupon.domain.Coupon;
import com.example.loyalty.coupon.domain.CouponDTO;
import com.example.loyalty.coupon.repository.CouponRepository;
import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.repository.RestaurantRepository;
import com.example.loyalty.user_loyalty.domain.UserLoyalty;
import com.example.loyalty.user_loyalty.repository.UserLoyaltyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final UserLoyaltyRepository userLoyaltyRepository;
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

    @Override
    public List<Coupon> getAllByRestaurantIdAndCouponLevel(Long restaurantId, Principal principal) {
        UserLoyalty userLoyalty = userLoyaltyRepository.findByRestaurantIdAndUserId(restaurantId, principal.getName());
        UserLoyalty.UserLoyaltyLevel level = userLoyalty.getLevel();
        List<Coupon> allCoupons = couponRepository.findByRestaurantId(restaurantId);

        return allCoupons.stream()
                .filter(coupon -> {
                    switch (level) {
                        case STANDARD -> {
                            return coupon.getLevel() == Coupon.Level.STANDARD;
                        }
                        case PREMIUM -> {
                            return coupon.getLevel() == Coupon.Level.STANDARD
                                    || coupon.getLevel() == Coupon.Level.PREMIUM;
                        }
                        case VIP -> {
                            return true;
                        }
                        default -> {
                            return false;
                        }
                    }
                })
                .toList();
    }

    private Coupon buildCoupon(CouponDTO couponDto) {
        Restaurant restaurant = restaurantRepository.findById(couponDto.restaurantId()).orElseThrow(() -> new EntityNotFoundException("Restaurant not found " + couponDto.restaurantId()));
        return Coupon.builder()
                .name(couponDto.name())
                .description(couponDto.description())
                .points(couponDto.points())
                .restaurant(restaurant)
                .level(couponDto.level())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
