package org.ff.fastfoodbackend.repositories;

import org.ff.fastfoodbackend.entities.food.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {
    Optional<Food> findByName(String name);
    boolean existsByName(String name);
}
