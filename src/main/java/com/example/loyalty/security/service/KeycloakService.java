package com.example.loyalty.security.service;

import com.example.loyalty.restaurant.domain.UpdateRestaurantAdminDTO;
import com.example.loyalty.security.constants.Constants;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
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

    public void assignRealmRoleToUser(String userId, String roleName) {
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        RoleRepresentation roleRepresentation = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        userResource.roles().realmLevel().add(List.of(roleRepresentation));
    }

    public void removeRealmRoleFromUser(String userId, String roleName) {
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        RoleRepresentation roleRepresentation = keycloak.realm(realm)
                .roles()
                .get(roleName)
                .toRepresentation();

        userResource.roles().realmLevel().remove(List.of(roleRepresentation));
    }

    public UserRepresentation getUserById(String userId) {
        return keycloak
                .realm(realm)
                .users()
                .get(userId)
                .toRepresentation();
    }

    public Set<UserRepresentation> getUsersByRealmRole(String roleName) {
        RoleResource roleResource = keycloak.realm(realm).roles().get(roleName);
        return roleResource.getRoleUserMembers();
    }

    public void deleteRestaurantAdmin(String userId) {
        UsersResource usersResource = keycloak.realm(realm).users();

        try {
            var user = usersResource.get(userId);
            if (user.toRepresentation() == null) {
                throw new EntityNotFoundException("User not found: " + userId);
            }

          removeRealmRoleFromUser(userId, Constants.RESTAURANT_ADMIN);
        } catch (NotFoundException e) {
            throw new EntityNotFoundException("User not found: " + userId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user: " + userId, e);
        }
    }

    public UserRepresentation userExistByUsername(String username) {
        UsersResource usersResource = keycloak.realm(realm).users();
        try {
            List<UserRepresentation> users = usersResource.search(username, 0, 1);
            return users.isEmpty() ? null : users.get(0);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user by username: " + username, e);
        }
    }

    public void updateRestaurantAdmin(String userId, UpdateRestaurantAdminDTO restaurantAdminDTO) {
        UsersResource usersResource = keycloak.realm(realm).users();
        UserResource userResource = usersResource.get(userId);

        // Fetch existing user
        UserRepresentation existingUser = userResource.toRepresentation();
        if (existingUser == null) {
            throw new EntityNotFoundException("User not found: " + userId);
        }

        existingUser.setEmail(restaurantAdminDTO.email());
        existingUser.setFirstName(restaurantAdminDTO.firstName());
        existingUser.setLastName(restaurantAdminDTO.lastName());

        userResource.update(existingUser);
    }

    public String createRestaurantAdmin(String username, String email, String firstName,String lastName, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(true);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        user.setCredentials(List.of(credential));

        UsersResource usersResource = keycloak.realm(realm).users();
        var response = usersResource.create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user: " + response.getStatus());
        }

        String locationHeader = response.getHeaderString("Location");
        return locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
    }

    public void assignVipCardByEmail(String email,String vipCardValue) {
        UsersResource usersResource = keycloak.realm(realm).users();
        List<UserRepresentation> users = usersResource.searchByEmail(email, true);

        if (users == null || users.isEmpty()) {
            log.info("No users found for email: {}", email);
            return;
        }

        UserRepresentation user = users.getFirst();
        UserResource userResource = usersResource.get(user.getId());

        Map<String, List<String>> attributes = user.getAttributes();
        if (attributes == null) {
            attributes = new java.util.HashMap<>();
        }
        attributes.put("vipCard", List.of(vipCardValue));
        user.setAttributes(attributes);
        userResource.update(user);
    }
}