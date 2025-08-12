package com.example.loyalty.dashboard.controller;

import com.example.loyalty.dashboard.domain.DashboardView;
import com.example.loyalty.dashboard.service.DashboardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/dashboards")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardServiceImpl dashboardService;

    @GetMapping("/{restaurantId}")
    public DashboardView getDashboardStatisticsForRestaurant(@PathVariable Long restaurantId, Principal principal) {
        return dashboardService.findDashboardStatisticsForRestaurant(restaurantId,principal);
    }
}
