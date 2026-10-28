package org.ff.fastfoodbackend.repositories;

import org.ff.fastfoodbackend.entities.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Restaurant> findByManagerId(UUID managerId);
}
