package com.example.loyalty.coupon.service;

import com.example.loyalty.coupon.domain.Coupon;
import com.example.loyalty.coupon.domain.CouponDTO;
import com.example.loyalty.coupon.repository.CouponRepository;
import com.example.loyalty.coupon.security.CouponRolePermissionChecker;
import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.repository.RestaurantRepository;
import com.example.loyalty.restaurant.security.RestaurantRolePermissionChecker;
import com.example.loyalty.user_loyalty.domain.UserLoyalty;
import com.example.loyalty.user_loyalty.repository.UserLoyaltyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CouponRolePermissionChecker rolePermissionsChecker;


    @Override
    public List<Coupon> findAllByRestaurantId(Long restaurantId) {
        return couponRepository.findByRestaurantId(restaurantId);
    }

    @Override
    @Transactional
    public Coupon create(CouponDTO couponDto,Principal principal) {
        rolePermissionsChecker.canCreateNewCoupon(principal);
        Coupon coupon = buildCoupon(couponDto);
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllByRestaurantIdAndCouponLevel(Long restaurantId, Principal principal) {
        UserLoyalty userLoyalty = userLoyaltyRepository.findByRestaurantIdAndUserId(restaurantId, principal.getName());
        if (userLoyalty == null) {
            throw new EntityNotFoundException("User loyalty not found for user=" + principal.getName() + ", restaurantId=" + restaurantId);
        }

        UserLoyalty.UserLoyaltyLevel level = userLoyalty.getLevel();
        List<Coupon> allCoupons = couponRepository.findByRestaurantId(restaurantId);

        return allCoupons.stream()
                .filter(coupon -> isCouponEligible(coupon, level))
                .toList();
    }

    private boolean isCouponEligible(Coupon coupon, UserLoyalty.UserLoyaltyLevel level) {
        return switch (level) {
            case STANDARD -> coupon.getLevel() == Coupon.Level.STANDARD;
            case PREMIUM -> coupon.getLevel() == Coupon.Level.STANDARD || coupon.getLevel() == Coupon.Level.PREMIUM;
            case VIP -> true;
            default -> false;
        };
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
