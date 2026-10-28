package org.ff.fastfoodbackend.repositories;

import org.ff.fastfoodbackend.entities.restaurant.Brand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void clean() {
        brandRepository.deleteAll();
        entityManager.flush();
    }

    @Test
    void saveAndQueryByName() {
        Brand b = Brand.builder().name("FastPizza").build();
        brandRepository.saveAndFlush(b);

        Optional<Brand> found = brandRepository.findByName("FastPizza");
        assertThat(found).isPresent();
        assertThat(brandRepository.existsByName("FastPizza")).isTrue();
    }

    @Test
    void uniqueNameConstraint() {
        Brand b1 = Brand.builder().name("UniqueBrand").build();
        brandRepository.saveAndFlush(b1);

        Brand b2 = Brand.builder().name("UniqueBrand").build();
        assertThatThrownBy(() -> brandRepository.saveAndFlush(b2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
