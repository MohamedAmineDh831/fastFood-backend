package org.ff.fastfoodbackend.repositories;

import org.ff.fastfoodbackend.entities.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
        entityManager.flush();
    }

//    @Test
//    void saveAndQueryByEmail() {
//        User u = User.builder().name("Alice").email("alice@example.com").build();
//        userRepository.saveAndFlush(u);
//
//        Optional<User> found = userRepository.findByEmail("alice@example.com");
//        assertThat(found).isPresent();
//        assertThat(userRepository.existsByEmail("alice@example.com")).isTrue();
//    }
//
//    @Test
//    void uniqueEmailConstraint() {
//        User u1 = User.builder().name("Bob").email("bob@example.com").build();
//        userRepository.saveAndFlush(u1);
//
//        User u2 = User.builder().name("Bobby").email("bob@example.com").build();
//
//        assertThatThrownBy(() -> {
//            userRepository.saveAndFlush(u2);
//        }).isInstanceOf(DataIntegrityViolationException.class);
//    }
}
