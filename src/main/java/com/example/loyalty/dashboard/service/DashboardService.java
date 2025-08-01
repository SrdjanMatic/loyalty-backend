package com.example.loyalty.dashboard.service;

import com.example.loyalty.dashboard.domain.Dashboard;

import java.security.Principal;

public interface DashboardService {
    Dashboard findDashboardStatisticsForRestaurant(Long restaurantId, Principal principal);
}
