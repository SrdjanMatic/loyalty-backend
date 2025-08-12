package com.example.loyalty.coupon.service;

import com.example.loyalty.coupon.domain.Coupon;
import com.example.loyalty.coupon.domain.CouponDTO;
import com.example.loyalty.coupon.domain.CouponView;
import com.example.loyalty.coupon.repository.CouponRepository;
import com.example.loyalty.coupon.security.CouponRolePermissionChecker;
import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.repository.RestaurantRepository;
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
    public List<CouponView> findAllByRestaurantId(Long restaurantId) {
        return couponRepository.findByRestaurantId(restaurantId);
    }

    @Override
    @Transactional
    public CouponView create(CouponDTO couponDto, Principal principal) {
        rolePermissionsChecker.canCreateAndUpdateCoupon(principal);
        Coupon coupon = buildCoupon(couponDto);
        Coupon saved = couponRepository.save(coupon);
        return couponRepository.findById(saved.getId(), CouponView.class)
                .orElseThrow(() -> new IllegalStateException("Created coupon not found"));

    }

    @Override
    @Transactional
    public CouponView update(Long id, CouponDTO couponDTO, Principal principal) {
        rolePermissionsChecker.canCreateAndUpdateCoupon(principal);
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Coupon Not Found"));

        coupon.setName(couponDTO.name());
        coupon.setDescription(couponDTO.description());
        coupon.setLevel(couponDTO.level());
        coupon.setPoints(couponDTO.points());
        coupon.setUpdatedAt(LocalDateTime.now());
        Coupon saved = couponRepository.save(coupon);

        return couponRepository.findById(saved.getId(), CouponView.class)
                .orElseThrow(() -> new IllegalStateException("Created coupon not found"));
    }

    @Override
    public List<CouponView> findAllByRestaurantIdAndCouponLevel(Long restaurantId, Principal principal) {
        UserLoyalty userLoyalty = userLoyaltyRepository.findByRestaurantIdAndUserId(restaurantId, principal.getName());
        if (userLoyalty == null) {
            throw new EntityNotFoundException("User loyalty not found for user=" + principal.getName() + ", restaurantId=" + restaurantId);
        }

        UserLoyalty.UserLoyaltyLevel level = userLoyalty.getLevel();
        List<CouponView> allCoupons = couponRepository.findByRestaurantId(restaurantId);

        return allCoupons.stream()
                .filter(coupon -> isCouponEligible(coupon, level))
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id, Principal principal) {
        rolePermissionsChecker.canDeleteCoupon(principal);

        couponRepository.deleteById(id);

    }



    private boolean isCouponEligible(CouponView coupon, UserLoyalty.UserLoyaltyLevel level) {
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
