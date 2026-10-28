package org.ff.fastfoodbackend.config.secutity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");

        if (realmAccess == null || realmAccess.isEmpty()) {
            return List.of();
        }

        return ((Collection<String>) realmAccess.get("roles")).stream()
                .map(roleName -> "ROLE_" + roleName) // Ajoute le préfixe ROLE_ pour Spring
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}