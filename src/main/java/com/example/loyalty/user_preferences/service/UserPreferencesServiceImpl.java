package com.example.loyalty.user_preferences.service;

import com.example.loyalty.security.service.KeycloakService;
import com.example.loyalty.user_preferences.domain.UserFoodPreferences;
import com.example.loyalty.user_preferences.domain.UserPreferencesDTO;
import com.example.loyalty.user_preferences.domain.UserVisitPreferences;
import com.example.loyalty.user_preferences.repository.UserFoodPreferencesRepository;
import com.example.loyalty.user_preferences.repository.UserVisitPreferencesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserPreferencesServiceImpl implements UserPreferencesService {

    private final UserFoodPreferencesRepository userFoodPreferencesRepository;
    private final UserVisitPreferencesRepository userVisitPreferencesRepository;
    private final KeycloakService keycloakService;

    @Override
    @Transactional
    public void create(UserPreferencesDTO userPreferencesDTO, Principal principal) {
        String userKeycloakId = principal.getName();

        saveFoodPreferences(userPreferencesDTO, userKeycloakId);
        saveVisitPreferences(userPreferencesDTO, userKeycloakId);
        Map<String, List<String>> customAttributes = new HashMap<>();
        customAttributes.put("surveyCompleted", Collections.singletonList("true"));
        keycloakService.addCustomAttributesToUser(customAttributes, principal.getName());
    }

    private void saveFoodPreferences(UserPreferencesDTO userPreferencesDTO, String userKeycloakId) {

        if (userPreferencesDTO.foodPreferences() == null || userPreferencesDTO.foodPreferences().isEmpty()) {
            log.warn("No food preferences provided for user: {}", userKeycloakId);
            return;
        }

        List<UserFoodPreferences> preferences = userPreferencesDTO.foodPreferences()
                .stream()
                .map(pref -> buildUserFoodPreference(pref, userKeycloakId))
                .toList();

        userFoodPreferencesRepository.saveAll(preferences);
    }


    private void saveVisitPreferences(UserPreferencesDTO dto, String userKeycloakId) {

        if (dto.visitPreferences() == null || dto.visitPreferences().isEmpty()) {
            log.warn("No visit preferences provided for user: {}", userKeycloakId);
            return;
        }

        List<UserVisitPreferences> visitEntities = dto.visitPreferences().stream()
                .map(pref -> {
                    String[] parts = pref.split("-");
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("Invalid visit preference format: " + pref);
                    }
                    return UserVisitPreferences.builder()
                            .userId(userKeycloakId)
                            .dayOfWeek(parts[0])
                            .partOfDay(parts[1])
                            .createdAt(LocalDateTime.now())
                            .build();
                }).toList();

        userVisitPreferencesRepository.saveAll(visitEntities);
    }

    private UserFoodPreferences buildUserFoodPreference(String preference, String userKeycloakId) {
        return UserFoodPreferences.builder()
                .userId(userKeycloakId)
                .preference(preference)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
