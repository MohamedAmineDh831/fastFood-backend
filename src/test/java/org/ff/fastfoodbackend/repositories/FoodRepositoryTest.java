package org.ff.fastfoodbackend.repositories;

import org.ff.fastfoodbackend.entities.food.Food;
import org.ff.fastfoodbackend.entities.food.FoodSubType;
import org.ff.fastfoodbackend.entities.food.FoodType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class FoodRepositoryTest {

    @Autowired private FoodRepository foodRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void clean() {
        foodRepository.deleteAll();
        entityManager.flush();
    }

    @Test
    void saveAndQueryByName() {
        Food f = Food.builder()
                .name("Neptune Pizza")
                .type(FoodType.PIZZA)
                .subType(FoodSubType.TUNA)
                .build();
        foodRepository.saveAndFlush(f);

        Optional<Food> found = foodRepository.findByName("Neptune Pizza");
        assertThat(found).isPresent();
        assertThat(foodRepository.existsByName("Neptune Pizza")).isTrue();
        assertThat(found.get().getName()).isEqualTo("Neptune Pizza");
        assertThat(found.get().getType()).isEqualTo(FoodType.PIZZA);
        assertThat(found.get().getSubType()).isEqualTo(FoodSubType.TUNA);
    }
}
