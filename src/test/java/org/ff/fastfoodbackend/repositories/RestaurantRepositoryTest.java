package org.ff.fastfoodbackend.repositories;

import org.ff.fastfoodbackend.entities.restaurant.Brand;
import org.ff.fastfoodbackend.entities.restaurant.Restaurant;
import org.ff.fastfoodbackend.entities.user.User;
import org.ff.fastfoodbackend.entities.user.UserRole;
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
class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BrandRepository brandRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void clean() {
        restaurantRepository.deleteAll();
        userRepository.deleteAll();
        brandRepository.deleteAll();
        entityManager.flush();
    }

//    private User saveManager(String name, String email) {
//        User u = User.builder().name(name).email(email).role(UserRole.RESTAURANT_MANAGER).build();
//        return userRepository.saveAndFlush(u);
//    }

    private Brand saveBrand(String name) {
        Brand b = Brand.builder().name(name).build();
        return brandRepository.saveAndFlush(b);
    }

//    @Test
//    void saveAndQueryByEmail_andByManagerId() {
//        User manager = saveManager("Manager 1", "mgr1@example.com");
//        Brand brand = saveBrand("BrandA");
//        Restaurant r = Restaurant.builder()
//                .address("123 Main St")
//                .email("r1@example.com")
//                .manager(manager)
//                .brand(brand)
//                .build();
//        restaurantRepository.saveAndFlush(r);
//
//        Optional<Restaurant> byEmail = restaurantRepository.findByEmail("r1@example.com");
//        assertThat(byEmail).isPresent();
//        assertThat(restaurantRepository.existsByEmail("r1@example.com")).isTrue();
//
//        Optional<Restaurant> byManager = restaurantRepository.findByManagerId(manager.getId());
//        assertThat(byManager).isPresent();
//        assertThat(byManager.get().getManager().getId()).isEqualTo(manager.getId());
//        assertThat(byManager.get().getBrand().getId()).isEqualTo(brand.getId());
//    }

//    @Test
//    void uniqueEmailConstraint() {
//        User manager = saveManager("Manager 1", "mgr1@example.com");
//        Brand brand = saveBrand("BrandA");
//        Restaurant r1 = Restaurant.builder()
//                .address("A1")
//                .email("dup@example.com")
//                .manager(manager)
//                .brand(brand)
//                .build();
//        restaurantRepository.saveAndFlush(r1);
//
//        // Need a different manager for second restaurant
//        User manager2 = saveManager("Manager 2", "mgr2@example.com");
//        Restaurant r2 = Restaurant.builder()
//                .address("A2")
//                .email("dup@example.com")
//                .manager(manager2)
//                .brand(brand)
//                .build();
//
//        assertThatThrownBy(() -> restaurantRepository.saveAndFlush(r2))
//                .isInstanceOf(DataIntegrityViolationException.class);
//    }
}
