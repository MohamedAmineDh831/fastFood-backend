package org.ff.fastfoodbackend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "FastFood Backend API",
                version = "v1",
                description = "REST API documentation for FastFood Backend",
                contact = @Contact(name = "FastFood Team"),
                license = @License(name = "MIT")
        ),
        servers = {
                // Attention : Si ton Spring tourne sur 8081, mets 8081 ici.
                // 8080 est généralement réservé à Keycloak.
                @Server(url = "http://localhost:8081", description = "Backend Server")
        },
        // On indique que TOUTES les APIs de la doc nécessitent cette sécurité par défaut
        security = @SecurityRequirement(name = "Bearer Authentication")
)
// On définit ce que signifie "Bearer Authentication"
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {

    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> {
            // On cherche si l'annotation PreAuthorize est présente sur la méthode
            PreAuthorize preAuthorize = handlerMethod.getMethodAnnotation(PreAuthorize.class);
            if (preAuthorize != null) {
                String currentDesc = operation.getDescription() != null ? operation.getDescription() : "";
                // On ajoute la condition SpEL au début de la description
                operation.setDescription(currentDesc + "\n\n**Security Requirement:** `" + preAuthorize.value() + "`");
            }
            return operation;
        };
    }
}