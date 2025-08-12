package com.example.loyalty.restaurant.service.vip_restaurant;

import com.example.loyalty.company.domain.Company;
import com.example.loyalty.coupon.domain.Coupon;
import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.domain.VipRestaurant;
import com.example.loyalty.restaurant.domain.VipRestaurantDTO;
import com.example.loyalty.restaurant.domain.VipRestaurantView;
import com.example.loyalty.restaurant.repository.RestaurantRepository;
import com.example.loyalty.restaurant.repository.VipRestaurantRepository;
import com.example.loyalty.restaurant.security.VipRestaurantRolePermissionChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class VipRestaurantServiceImpl implements VipRestaurantService {

    private final VipRestaurantRepository vipRestaurantRepository;
    private final RestaurantRepository restaurantRepository;
    private final VipRestaurantRolePermissionChecker rolePermissionChecker;


    @Override
    public List<VipRestaurantView> findAll() {
        return vipRestaurantRepository.findAllBy(VipRestaurantView.class);
    }

    @Override
    public VipRestaurantView create(VipRestaurantDTO vipRestaurantDTO, Principal principal) {
        if (vipRestaurantRepository.findByRestaurantId(vipRestaurantDTO.restaurantId()).isPresent()) {
            throw new IllegalStateException("Restaurant with this ID already exists.");
        }
        rolePermissionChecker.canCreateUpdateDeleteVipRestaurant(principal);
        VipRestaurant vipRestaurant = buildVipRestaurant(vipRestaurantDTO);
        VipRestaurant saved = vipRestaurantRepository.save(vipRestaurant);
        return vipRestaurantRepository.findById(saved.getId(), VipRestaurantView.class)
                .orElseThrow(() -> new IllegalStateException("Saved vip restaurant not found"));
    }

    @Override
    public void delete(Long id, Principal principal) {
        rolePermissionChecker.canCreateUpdateDeleteVipRestaurant(principal);

        vipRestaurantRepository.deleteById(id);
    }

    @Override
    public VipRestaurantView update(Long id, VipRestaurantDTO vipRestaurantDTO, Principal principal) {
        rolePermissionChecker.canCreateUpdateDeleteVipRestaurant(principal);

        VipRestaurant vipRestaurant = vipRestaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vip restaurant not found with id: " + id));

        Restaurant restaurant = restaurantRepository.findById(vipRestaurantDTO.restaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found " + vipRestaurantDTO.restaurantId()));

        Map<Coupon.Level, BigDecimal> levelDiscounts = getLevelBigDecimalMap(vipRestaurantDTO);

        vipRestaurant.setRestaurant(restaurant);
        vipRestaurant.setGeneralDiscount(vipRestaurantDTO.discount());
        vipRestaurant.setBackgroundImage(vipRestaurantDTO.backgroundImage());
        vipRestaurant.getLevelDiscounts().clear();
        if (levelDiscounts != null) {
            vipRestaurant.getLevelDiscounts().putAll(levelDiscounts);
        }
        vipRestaurant.setUpdatedAt(LocalDateTime.now());

        VipRestaurant saved = vipRestaurantRepository.save(vipRestaurant);

        return vipRestaurantRepository.findById(saved.getId(), VipRestaurantView.class)
                .orElseThrow(() -> new IllegalStateException("Saved vip restaurant not found"));

    }

    private VipRestaurant buildVipRestaurant(VipRestaurantDTO vipRestaurantDTO) {
        Restaurant restaurant = restaurantRepository.findById(vipRestaurantDTO.restaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found " + vipRestaurantDTO.restaurantId()));

        Map<Coupon.Level, BigDecimal> levelDiscounts = getLevelBigDecimalMap(vipRestaurantDTO);

        return VipRestaurant.builder()
                .restaurant(restaurant)
                .generalDiscount(vipRestaurantDTO.discount())
                .backgroundImage(vipRestaurantDTO.backgroundImage())
                .levelDiscounts(levelDiscounts)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static Map<Coupon.Level, BigDecimal> getLevelBigDecimalMap(VipRestaurantDTO vipRestaurantDTO) {
        Map<Coupon.Level, BigDecimal> levelDiscounts = null;

        if (vipRestaurantDTO.discount() == null) {
            levelDiscounts = Map.of(
                    Coupon.Level.STANDARD, vipRestaurantDTO.standardDiscount(),
                    Coupon.Level.VIP, vipRestaurantDTO.vipDiscount(),
                    Coupon.Level.PREMIUM, vipRestaurantDTO.premiumDiscount()
            );
        }
        return levelDiscounts;
    }
}
