package org.ff.fastfoodbackend.services;

import jakarta.ws.rs.core.Response;
import org.ff.fastfoodbackend.dto.RegisterUserDto;
import org.ff.fastfoodbackend.entities.user.Customer;
import org.ff.fastfoodbackend.entities.user.User;
import org.ff.fastfoodbackend.repositories.UserRepository;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Value("${app.keycloak.admin.server-url}")
    private String serverUrl;

    @Value("${app.keycloak.admin.realm}")
    private String adminRealm;

    @Value("${app.keycloak.admin.username}")
    private String adminUsername;

    @Value("${app.keycloak.admin.password}")
    private String adminPassword;

    @Value("${app.keycloak.admin.realm}")
    private String realm;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public void registerUser(RegisterUserDto registerUserDto) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(adminRealm)
                .clientId("admin-cli")
                .username(adminUsername)
                .password(adminPassword)
                .build();


        UserRepresentation userRepresentation = getUserRepresentation(registerUserDto);

        Response response = keycloak.realm(realm).users().create(userRepresentation);

        if (response.getStatus() == 201) {
            String userId = CreatedResponseUtil.getCreatedId(response);

            RoleRepresentation role = keycloak.realm(realm)
                    .roles()
                    .get(registerUserDto.getRole().getSecurityName())
                    .toRepresentation();

            keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(role));

            User user = registerUserDto.toEntity();
            user.setId(UUID.fromString(userId));
            user.setAddress(registerUserDto.getAddress());
            userRepository.save(user);
        } else {
            throw new RuntimeException("Failed to create user in Keycloak. Status: " + response.getStatus());
        }
    }

    private static UserRepresentation getUserRepresentation(RegisterUserDto request) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));
        return user;
    }

//    public User update(Long id, UserDto request) {
//        User existing = userRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//
//        if (request.getName() != null && !request.getName().isBlank()) {
//            existing.setName(request.getName().trim());
//        }
//        if (request.getEmail() != null && !request.getEmail().isBlank()) {
//            String newEmail = request.getEmail().trim();
//            if (!newEmail.equalsIgnoreCase(existing.getEmail()) && userRepository.existsByEmail(newEmail)) {
//                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
//            }
//            existing.setEmail(newEmail);
//        }
//        if (request.getRole() != null && !request.getRole().isBlank()) {
//            existing.setRole(parseRole(request.getRole()));
//        }
//        return userRepository.save(existing);
//    }
//
//    public void delete(Long id) {
//        if (!userRepository.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
//        }
//        userRepository.deleteById(id);
//    }
//
//    private UserRole parseRole(String roleStr) {
//        try {
//            return UserRole.valueOf(roleStr.trim().toUpperCase());
//        } catch (IllegalArgumentException ex) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//                    "Invalid role. Allowed: ADMIN, DELIVERY, RESTAURANT_MANAGER, CUSTOMER");
//        }
//    }
//
//    private UserRole parseRoleOrDefault(String roleStr) {
//        if (roleStr == null || roleStr.isBlank()) {
//            return UserRole.CUSTOMER;
//        }
//        return parseRole(roleStr);
//    }
}
