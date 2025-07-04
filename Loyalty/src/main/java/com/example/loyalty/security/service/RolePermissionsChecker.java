package com.example.loyalty.security.service;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.Map;


@Component
public class RolePermissionsChecker {
    public List<String> getRoles(Principal principal) {
        List<String> roles = List.of();

        if (principal instanceof JwtAuthenticationToken jwtAuth) {
            Object realmAccess = jwtAuth.getToken().getClaim("realm_access");
            if (realmAccess instanceof Map<?, ?> map) {
                Object rolesObj = map.get("roles");
                if (rolesObj instanceof List<?> rolesList) {
                    roles = rolesList.stream().map(Object::toString).toList();
                }
            }
        }
        return roles;
    }
}
