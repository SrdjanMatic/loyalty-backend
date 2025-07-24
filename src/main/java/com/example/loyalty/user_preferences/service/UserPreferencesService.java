package com.example.loyalty.user_preferences.service;

import com.example.loyalty.user_preferences.domain.UserPreferencesDTO;

import java.security.Principal;


public interface UserPreferencesService {

    void create(UserPreferencesDTO userPreferencesDTO, Principal principal);
}