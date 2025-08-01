package com.example.loyalty.restaurant.service.restaurant_config;

import com.example.loyalty.receipt.repository.ReceiptRepository;
import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.domain.restaurant_config.*;
import com.example.loyalty.restaurant.repository.ChallengeTemplateRepository;
import com.example.loyalty.restaurant.repository.RestaurantConfigRepository;
import com.example.loyalty.restaurant.repository.RestaurantRepository;
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
public class RestaurantConfigServiceImpl implements RestaurantConfigService {

    private final RestaurantConfigRepository configRepository;
    private final RestaurantRepository restaurantRepository;
    private final ChallengeTemplateRepository challengeTemplateRepository;
    private final ReceiptRepository receiptRepository;

    @Override
    @Transactional
    public RestaurantConfig createRestaurantConfig(RestaurantConfigDTO restaurantConfigDTO, Principal principal) {

        Restaurant restaurant = restaurantRepository.findById(restaurantConfigDTO.restaurantId())
                .orElseThrow((() -> new EntityNotFoundException("Restaurant not found with id:" + restaurantConfigDTO.restaurantId())));

        RestaurantConfig savedConfig = buildRestaurantConfig(restaurantConfigDTO, restaurant);

        //TODO: add this to config response
        List<ChallengeTemplate> challengeTemplates = buildChallengeTemplates(restaurantConfigDTO, savedConfig);

        return savedConfig;
    }

    private List<ChallengeTemplate> buildChallengeTemplates(RestaurantConfigDTO restaurantConfigDTO, RestaurantConfig savedConfig) {
        List<ChallengeTemplate> challenges = restaurantConfigDTO.challengeList().stream()
                .map(challengeDTO -> ChallengeTemplate.builder()
                        .period(challengeDTO.period())
                        .visitsRequired(challengeDTO.visitsRequired())
                        .restaurantConfig(savedConfig)
                        .build())
                .toList();

        return challengeTemplateRepository.saveAll(challenges);
    }

    private RestaurantConfig buildRestaurantConfig(RestaurantConfigDTO restaurantConfigDTO, Restaurant restaurant) {
        RestaurantConfig config = configRepository.findByRestaurantId(restaurant.getId())
                .orElseGet(() -> RestaurantConfig.builder()
                        .restaurant(restaurant)
                        .build());

        config.setLogo(restaurantConfigDTO.logo());
        config.setDescription(restaurantConfigDTO.description());
        config.setRestaurantDisplayName(restaurantConfigDTO.restaurantDisplayName());
        config.setBackgroundImage(restaurantConfigDTO.backgroundImage());
        config.setFontColor(restaurantConfigDTO.fontColor());
        config.setBackgroundColor(restaurantConfigDTO.backgroundColor());
        config.setHeaderAndButtonColor(restaurantConfigDTO.headerAndButtonColor());

        return configRepository.save(config);
    }

    @Override
    public List<ChallengeTemplateView> findRestaurantChallenge(Long restaurantId, Principal principal) {
        return challengeTemplateRepository.findByRestaurantConfigRestaurantId(restaurantId);
    }

    @Override
    public List<ChallengeTemplateUserView> findRestaurantChallengeForUser(Long restaurantId, Principal principal) {
        String userId = principal.getName();
        LocalDateTime now = LocalDateTime.now();

        return challengeTemplateRepository.findByRestaurantConfigRestaurantId(restaurantId).stream()
                .map(template -> {
                    LocalDateTime afterDate = now.minusDays(template.getPeriod());
                    long visitsCompleted = receiptRepository.countUnusedReceiptsAfterDate(
                            restaurantId, template.getId(), userId, afterDate);

                    return ChallengeTemplateUserView.builder()
                            .id(template.getId())
                            .period(template.getPeriod())
                            .visitsRequired(template.getVisitsRequired())
                            .visitsCompleted(visitsCompleted)
                            .build();
                })
                .toList();
    }
}
