package com.example.loyalty.dashboard.service;

import com.example.loyalty.dashboard.domain.DashboardView;

import java.security.Principal;

public interface DashboardService {
    DashboardView findDashboardStatisticsForRestaurant(Long restaurantId, Principal principal);
}
