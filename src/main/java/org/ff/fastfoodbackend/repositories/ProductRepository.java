package org.ff.fastfoodbackend.repositories;

import org.ff.fastfoodbackend.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByRestaurantId(Long restaurantId);
    Optional<Product> findByRestaurantIdAndFoodId(Long restaurantId, Long foodId);
    boolean existsByRestaurantIdAndFoodId(Long restaurantId, Long foodId);
}