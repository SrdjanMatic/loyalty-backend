package com.example.loyalty.employee.security;

import com.example.loyalty.security.service.RolePermissionsChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

import static com.example.loyalty.security.constants.Constants.SYSTEM_ADMIN;

@Component
@RequiredArgsConstructor
public class EmployeeRolePermissionChecker {

    private final RolePermissionsChecker rolePermissionsChecker;

    public void canViewAllEmployees(Principal principal) {
        checkIfUserIsSystemAdmin(principal);
    }

    public void canCreateEmployee(Principal principal) {
        checkIfUserIsSystemAdmin(principal);
    }

    public void canDeleteEmployee(Principal principal) {
        checkIfUserIsSystemAdmin(principal);
    }

    private void checkIfUserIsSystemAdmin(Principal principal) {
        List<String> roles = rolePermissionsChecker.getRoles(principal);

        if (!roles.contains(SYSTEM_ADMIN)) {
            throw new AccessDeniedException("Unauthorized: You do not have permission for this action.");
        }
    }
}
