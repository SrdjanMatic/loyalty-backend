package com.example.loyalty.restaurant.service;

import com.example.loyalty.coupon.domain.Coupon;
import com.example.loyalty.coupon.repository.CouponRepository;
import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.domain.RestaurantDTO;
import com.example.loyalty.restaurant.domain.RestaurantWithUserLoyaltyView;
import com.example.loyalty.restaurant.repository.RestaurantRepository;
import com.example.loyalty.restaurant.security.RestaurantRolePermissionChecker;
import com.example.loyalty.security.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;


@Service
@RequiredArgsConstructor
@Log4j2
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRolePermissionChecker rolePermissionsChecker;
    private final RestaurantRepository restaurantRepository;
    private final CouponRepository couponRepository;
    private final KeycloakService keycloakService;

    @Override
    @Transactional
    public Restaurant createRestaurant(RestaurantDTO restaurantDTO, Principal principal) {
        rolePermissionsChecker.canCreateNewRestaurant(principal);
        String adminUserForRestaurant = keycloakService.createAdminUserForRestaurant(restaurantDTO.name(), restaurantDTO.name() + "@gmail.com", restaurantDTO.name(), restaurantDTO.name() + "123!");
        Restaurant buildRestaurant = getBuildRestaurant(restaurantDTO, adminUserForRestaurant);
        Restaurant savedRestaurant = restaurantRepository.save(buildRestaurant);

        addRestaurantIdToKeycloakAdminAccount(savedRestaurant, adminUserForRestaurant);
        generateDefaultCoupons(savedRestaurant);
        return savedRestaurant;
    }

    private static Restaurant getBuildRestaurant(RestaurantDTO restaurantDTO, String adminKeycloakId) {
        Restaurant buildRestaurant = Restaurant.builder()
                .name(restaurantDTO.name())
                .pib(restaurantDTO.pib())
                .address(restaurantDTO.address())
                .phone(restaurantDTO.phone())
                .adminKeycloakId(adminKeycloakId)
                .build();
        return buildRestaurant;
    }

    private void addRestaurantIdToKeycloakAdminAccount(Restaurant savedRestaurant, String adminUserForRestaurant) {
        Map<String, List<String>> customAttributes = new HashMap<>();
        customAttributes.put("restaurantId", Collections.singletonList(savedRestaurant.getId().toString()));
        keycloakService.addCustomAttributesToUser(customAttributes, adminUserForRestaurant);
    }


    @Override
    public List<RestaurantWithUserLoyaltyView> getAllRestaurants(Principal principal) {
        rolePermissionsChecker.canViewAllRestaurants(principal);
        return restaurantRepository.findAllWithUserLoyalty(principal.getName());
    }

    @Override
    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }

    @Override
    public Restaurant updateRestaurant(Long id, Restaurant updatedRestaurant) {
        return restaurantRepository.findById(id)
                .map(existing -> {
                    existing.setName(updatedRestaurant.getName());
                    existing.setAddress(updatedRestaurant.getAddress());
                    return restaurantRepository.save(existing);
                }).orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    @Override
    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }

    private void generateDefaultCoupons(Restaurant restaurant) {
        if (restaurant == null || restaurant.getId() == null) {
            log.warn("Cannot generate coupons: restaurant is null or not persisted.");
            return;
        }

        record CouponDef(String name, int points, String description) {
        }

        List<CouponDef> defaultCoupons = List.of(
                new CouponDef("Kafa uz doručak", 100, "Besplatna kafa uz dorucak"),
                new CouponDef("Besplatan kolač", 200, "Besplatan kolac dana"),
                new CouponDef("10% popusta na burgere", 100, "Na svaki burger popust 10%")
        );

        List<Coupon> coupons = defaultCoupons.stream()
                .map(def -> Coupon.builder()
                        .name(def.name())
                        .description(def.description())
                        .points(def.points())
                        .restaurant(restaurant)
                        .build())
                .toList();

        couponRepository.saveAll(coupons);
        log.info("Generated {} default coupons for restaurant '{}'", coupons.size(), restaurant.getName());
    }

}
