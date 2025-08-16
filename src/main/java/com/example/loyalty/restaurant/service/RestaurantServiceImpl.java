package com.example.loyalty.restaurant.service;

import com.example.loyalty.coupon.domain.Coupon;
import com.example.loyalty.coupon.repository.CouponRepository;
import com.example.loyalty.restaurant.domain.*;
import com.example.loyalty.restaurant.domain.restaurant_config.ChallengeTemplate;
import com.example.loyalty.restaurant.domain.restaurant_config.ChallengeTemplateView;
import com.example.loyalty.restaurant.domain.restaurant_config.RestaurantConfig;
import com.example.loyalty.restaurant.domain.restaurant_config.RestaurantConfigDataView;
import com.example.loyalty.restaurant.repository.ChallengeTemplateRepository;
import com.example.loyalty.restaurant.repository.RestaurantConfigRepository;
import com.example.loyalty.restaurant.repository.RestaurantRepository;
import com.example.loyalty.restaurant.security.RestaurantRolePermissionChecker;
import com.example.loyalty.security.constants.Constants;
import com.example.loyalty.security.service.KeycloakService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Log4j2
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRolePermissionChecker rolePermissionsChecker;
    private final RestaurantRepository restaurantRepository;
    private final ChallengeTemplateRepository challengeTemplateRepository;
    private final RestaurantConfigRepository restaurantConfigRepository;
    private final CouponRepository couponRepository;
    private final KeycloakService keycloakService;

    @Override
    @Transactional
    public Restaurant create(CreateRestaurantDTO restaurantDTO, Principal principal) {
        rolePermissionsChecker.canCreateNewRestaurant(principal);

        Restaurant buildRestaurant = buildRestaurant(restaurantDTO, restaurantDTO.adminKeycloakId());
        Restaurant savedRestaurant = restaurantRepository.save(buildRestaurant);

        RestaurantConfig restaurantConfig = buildRestaurantConfig(savedRestaurant);
        RestaurantConfig savedRestaurantConfig = restaurantConfigRepository.save(restaurantConfig);

        ChallengeTemplate challengeTemplate = buildChallengeTemplate(savedRestaurantConfig);
        challengeTemplateRepository.save(challengeTemplate);

        addRestaurantIdToKeycloakAdminAccount(savedRestaurant, restaurantDTO.adminKeycloakId());
        generateDefaultCoupons(savedRestaurant);
        return savedRestaurant;
    }

    @Override
    public Restaurant update(Long restaurantId, UpdateRestaurantDTO restaurantDTO, Principal principal) {
        rolePermissionsChecker.canUpdateRestaurant(principal);

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        restaurant.setPhone(restaurantDTO.phone());
        restaurant.setName(restaurantDTO.name());
        restaurant.setAddress(restaurantDTO.address());
        restaurant.setAdminKeycloakId(restaurantDTO.adminKeycloakId());

        return restaurantRepository.save(restaurant);



    }

    private ChallengeTemplate buildChallengeTemplate(RestaurantConfig savedRestaurantConfig) {
        return ChallengeTemplate.builder()
                .restaurantConfig(savedRestaurantConfig)
                .period(7)
                .visitsRequired(3)
                .build();
    }

    private RestaurantConfig buildRestaurantConfig(Restaurant savedRestaurant) {
        return RestaurantConfig.builder()
                .fontColor("#222")
                .restaurant(savedRestaurant)
                .backgroundColor("#ffffff")
                .headerAndButtonColor("#bfa16b")
                .restaurantDisplayName(savedRestaurant.getName())
                .description("Vaši računi i lojalnost")
                .build();
    }

    private Restaurant buildRestaurant(CreateRestaurantDTO restaurantDTO, String adminKeycloakId) {
        return Restaurant.builder()
                .name(restaurantDTO.name())
                .pib(restaurantDTO.pib())
                .address(restaurantDTO.address())
                .premiumCouponLimit(500L)
                .vipCouponLimit(800L)
                .phone(restaurantDTO.phone())
                .adminKeycloakId(adminKeycloakId)
                .build();
    }

    private void addRestaurantIdToKeycloakAdminAccount(Restaurant savedRestaurant, String adminUserForRestaurant) {
        Map<String, List<String>> customAttributes = new HashMap<>();
        customAttributes.put("restaurantId", Collections.singletonList(savedRestaurant.getId().toString()));
        keycloakService.addCustomAttributesToUser(customAttributes, adminUserForRestaurant);
    }

    @Override
    public List<Restaurant> findAllRestaurants(Principal principal) {
        return restaurantRepository.findAll();
    }


    @Override
    public List<RestaurantWithUserLoyaltyView> findAllRestaurantsWithUserLoyalty(Principal principal) {
        return restaurantRepository.findAllWithUserLoyalty(principal.getName());
    }

    @Override
    public Restaurant findRestaurantById(Long id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
    }



    @Override
    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }

    @Override
    public RestaurantCouponLevelView findCouponLevel(Long restaurantId, Principal principal) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        return RestaurantCouponLevelView.builder()
                .premiumCouponLimit(restaurant.getPremiumCouponLimit())
                .vipCouponLimit(restaurant.getVipCouponLimit())
                .build();
    }

    @Override
    @Transactional
    public void updateCouponLimit(Long id, RestaurantCouponLevelDTO restaurantCouponLevel) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        restaurant.setPremiumCouponLimit(restaurantCouponLevel.premiumCouponLimit());
        restaurant.setVipCouponLimit(restaurantCouponLevel.vipCouponLimit());
        restaurantRepository.save(restaurant);
    }

    @Override
    @Transactional
    public RestaurantAdminView createRestaurantAdmin(CreateRestaurantAdminDTO restaurantAdminDTO, Principal principal) {
        rolePermissionsChecker.canCreateOrUpdateRestaurantAdmin(principal);

        UserRepresentation userExist = keycloakService.userExistByUsername(restaurantAdminDTO.username());
        String adminKeycloakId;
        if (userExist == null) {
            adminKeycloakId = keycloakService.createRestaurantAdmin(restaurantAdminDTO.username(), restaurantAdminDTO.email(), restaurantAdminDTO.firstName(), restaurantAdminDTO.lastName(), restaurantAdminDTO.username() + "123!");
            if (adminKeycloakId == null) {
                throw new RuntimeException("Failed to create restaurant admin user in Keycloak.");
            }
        } else {
            adminKeycloakId = userExist.getId();
        }

        keycloakService.assignRealmRoleToUser(adminKeycloakId, Constants.RESTAURANT_ADMIN);

        UserRepresentation userById = keycloakService.getUserById(adminKeycloakId);

        return RestaurantAdminView.builder()
                .username(userById.getUsername())
                .email(userById.getEmail())
                .firstName(userById.getFirstName())
                .keycloakId(adminKeycloakId)
                .lastName(userById.getLastName())
                .build();
    }

    @Override
    @Transactional
    public RestaurantAdminView updateRestaurantAdmin(String id, UpdateRestaurantAdminDTO restaurantAdminDTO, Principal principal) {
        rolePermissionsChecker.canCreateOrUpdateRestaurantAdmin(principal);

        keycloakService.updateRestaurantAdmin(id, restaurantAdminDTO);

        UserRepresentation userById = keycloakService.getUserById(id);


        return RestaurantAdminView.builder()
                .username(userById.getUsername())
                .email(userById.getEmail())
                .firstName(userById.getFirstName())
                .keycloakId(id)
                .lastName(userById.getLastName())
                .build();
    }



    @Override
    @Transactional
    public void deleteRestaurantAdmin(String id, Principal principal) {
        rolePermissionsChecker.canDeleteRestaurantAdmin(principal);

        restaurantRepository.findByAdminKeycloakId(id)
                .ifPresent(r -> {
                    throw new IllegalStateException("Cannot delete admin because restaurant uses admin");
                });
        keycloakService.deleteRestaurantAdmin(id);
    }



    public Set<RestaurantAdminView> findAllRestaurantAdmins(Principal principal) {
        return keycloakService.getUsersByRealmRole(Constants.RESTAURANT_ADMIN).stream()
                .map(user -> {
                    String restaurantName = getRestaurantNameFromCustomAttributes(user);

                    return RestaurantAdminView.builder()
                            .keycloakId(user.getId())
                            .firstName(user.getFirstName())
                            .username(user.getUsername())
                            .lastName(user.getLastName())
                            .email(user.getEmail())
                            .restaurantName(restaurantName)
                            .build();
                })
                .collect(Collectors.toSet());
    }

    @Override
    public RestaurantConfigDataView findRestaurantConfigData(Long restaurantId, Principal principal) {
        RestaurantConfigDataView config = restaurantRepository
                .findConfigDataRestaurant(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant config data view not found with id " + restaurantId));

        List<ChallengeTemplateView> challenges = challengeTemplateRepository.findByRestaurantConfigRestaurantId(config.getId());
        config.setChallengeList(challenges);

        return config;
    }


    private String getRestaurantNameFromCustomAttributes(UserRepresentation user) {
        String restaurantIdStr = Optional.ofNullable(user.getAttributes())
                .map(attrs -> attrs.get("restaurantId"))
                .filter(list -> !list.isEmpty())
                .map(List::getFirst)
                .orElse(null);

        String restaurantName = null;
        if (restaurantIdStr != null) {
            try {
                Long restaurantId = Long.parseLong(restaurantIdStr);
                restaurantName = restaurantRepository.findById(restaurantId)
                        .map(Restaurant::getName)
                        .orElse(null);
            } catch (NumberFormatException e) {
                // Handle invalid restaurantId format
                log.warn("Invalid restaurantId format for user {}", user.getUsername());
            }
        }
        return restaurantName;
    }

    private void generateDefaultCoupons(Restaurant restaurant) {
        if (restaurant == null || restaurant.getId() == null) {
            log.warn("Cannot generate coupons: restaurant is null or not persisted.");
            return;
        }

        record CouponDef(String name, int points, String description, Coupon.Level level) {
        }

        List<CouponDef> defaultCoupons = List.of(
                new CouponDef("Kafa uz doručak", 100, "Besplatna kafa uz dorucak", Coupon.Level.STANDARD),
                new CouponDef("Besplatan kolač", 200, "Besplatan kolac dana", Coupon.Level.STANDARD),
                new CouponDef("10% popusta na burgere", 200, "Na svaki burger popust 10%", Coupon.Level.PREMIUM),
                new CouponDef("20% popusta na racun", 300, "Popust na racun 20%", Coupon.Level.VIP)
        );

        List<Coupon> coupons = defaultCoupons.stream()
                .map(def -> (Coupon) Coupon.builder()
                        .name(def.name())
                        .description(def.description())
                        .level(def.level)
                        .points(def.points())
                        .restaurant(restaurant)
                        .createdAt(LocalDateTime.now())
                        .build())
                .toList();

        couponRepository.saveAll(coupons);
        log.info("Generated {} default coupons for restaurant '{}'", coupons.size(), restaurant.getName());
    }

}
