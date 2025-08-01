package com.example.loyalty.user_preferences.controller;

import com.example.loyalty.user_preferences.domain.UserPreferencesDTO;
import com.example.loyalty.user_preferences.service.UserPreferencesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/user-preference")
@RequiredArgsConstructor
public class UserPreferencesController {

    private final UserPreferencesService service;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody UserPreferencesDTO userPreferencesDTO, Principal principal) {
        service.create(userPreferencesDTO, principal);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
