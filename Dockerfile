# ==========================================
# STAGE 1 : BUILD (JDK 26 + Maven wrapper)
# ==========================================
FROM eclipse-temurin:26-jdk AS build
WORKDIR /app

# Copier d'abord le wrapper et le pom pour profiter du cache Docker des dépendances
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
# Retirer d'éventuels CRLF (checkout Windows) et rendre le wrapper exécutable
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B dependency:go-offline

# Copier les sources puis construire le jar (tests exécutés séparément en CI)
COPY src src
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B package -DskipTests \
    && cp target/*.jar app.jar

# Extraire les couches Spring Boot pour un démarrage et un cache plus efficaces
RUN java -Djarmode=tools -jar app.jar extract --layers --launcher --destination extracted

# ==========================================
# STAGE 2 : RUNTIME (JRE 26 uniquement)
# ==========================================
FROM eclipse-temurin:26-jre
LABEL authors="moham"
WORKDIR /app

# Utilisateur non-root
RUN groupadd --system spring && useradd --system --gid spring spring

COPY --from=build --chown=spring:spring /app/extracted/dependencies/ ./
COPY --from=build --chown=spring:spring /app/extracted/spring-boot-loader/ ./
COPY --from=build --chown=spring:spring /app/extracted/snapshot-dependencies/ ./
COPY --from=build --chown=spring:spring /app/extracted/application/ ./

USER spring

# Port défini dans application.properties (server.port=8081)
EXPOSE 8081

# Les valeurs de application.properties pointent vers localhost : à surcharger
# au lancement (ex. SPRING_DATASOURCE_URL, SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI,
# APP_KEYCLOAK_ADMIN_SERVER_URL...) pour joindre PostgreSQL et Keycloak depuis le conteneur.
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]
