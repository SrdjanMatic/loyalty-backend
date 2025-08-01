package com.example.loyalty.dashboard.security;

import com.example.loyalty.security.service.RolePermissionsChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

import static com.example.loyalty.security.constants.Constants.RESTAURANT_ADMIN;
import static com.example.loyalty.security.constants.Constants.SYSTEM_ADMIN;

@Component
@RequiredArgsConstructor
public class DashboardRolePermissionChecker {

    private final RolePermissionsChecker rolePermissionsChecker;

    public void canViewAllRestaurantsStatistics(Principal principal) {
        checkIfUserIsSystemAdminOrRestaurantAdmin(principal);
    }

    private void checkIfUserIsSystemAdminOrRestaurantAdmin(Principal principal) {
        List<String> roles = rolePermissionsChecker.getRoles(principal);

        if (!roles.contains(SYSTEM_ADMIN) && !roles.contains(RESTAURANT_ADMIN)) {
            throw new AccessDeniedException("Unauthorized: You do not have permission for this action.");
        }
    }

}
