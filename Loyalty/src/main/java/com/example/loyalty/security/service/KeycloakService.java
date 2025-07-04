package com.example.loyalty.security.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class KeycloakService {

    private Keycloak keycloak;
    private final String realm = "loyalty";


    public void addCustomAttributesToUser(Map<String, List<String>> customAttributes, String userId) {
        UsersResource usersResource = keycloak.realm(realm).users();
        UserResource userResource = usersResource.get(userId);
        UserRepresentation user = userResource.toRepresentation();
        user.setAttributes(customAttributes);
        userResource.update(user);
    }

    public String createAdminUserForRestaurant(String username, String email, String firstName, String password) {
        // Prepare user data
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setEnabled(true);

        // Prepare password credentials
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        user.setCredentials(List.of(credential));

        // Create user
        UsersResource usersResource = keycloak.realm(realm).users();
        var response = usersResource.create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user: " + response.getStatus());
        }

        // Extract userId from Location header
        String locationHeader = response.getHeaderString("Location");
        String userId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
        return userId;
    }
}