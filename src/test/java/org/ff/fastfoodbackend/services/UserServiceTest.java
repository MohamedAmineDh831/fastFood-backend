package org.ff.fastfoodbackend.services;

import org.ff.fastfoodbackend.dto.UserDto;
import org.ff.fastfoodbackend.entities.user.User;
import org.ff.fastfoodbackend.entities.user.UserRole;
import org.ff.fastfoodbackend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

//    @Test
//    void create_defaultsRoleAndTrims() {
//        UserDto dto = new UserDto();
//        dto.setName("  Alice  ");
//        dto.setEmail(" alice@example.com ");
//        User u = userService.create(dto);
//        assertThat(u.getId()).isNotNull();
//        assertThat(u.getName()).isEqualTo("Alice");
//        assertThat(u.getEmail()).isEqualTo("alice@example.com");
//        assertThat(u.getRole()).isEqualTo(UserRole.CUSTOMER);
//    }
//
//    @Test
//    void create_withRole_caseInsensitive() {
//        UserDto dto = new UserDto();
//        dto.setName("Bob");
//        dto.setEmail("bob@example.com");
//        dto.setRole("admin");
//        User u = userService.create(dto);
//        assertThat(u.getRole()).isEqualTo(UserRole.ADMIN);
//    }
//
//    @Test
//    void create_duplicateEmail_conflict() {
//        UserDto d1 = new UserDto();
//        d1.setName("C1"); d1.setEmail("c@example.com");
//        userService.create(d1);
//        UserDto d2 = new UserDto();
//        d2.setName("C2"); d2.setEmail("c@example.com");
//        assertThatThrownBy(() -> userService.create(d2))
//                .isInstanceOf(ResponseStatusException.class)
//                .hasMessageContaining(String.valueOf(HttpStatus.CONFLICT.value()));
//    }
//
//    @Test
//    void update_changeEmail_conflictOnDuplicate() {
//        User u1 = userService.create(dto("U1","u1@ex.com", null));
//        User u2 = userService.create(dto("U2","u2@ex.com", null));
//        UserDto upd = new UserDto(); upd.setEmail("u1@ex.com");
//        assertThatThrownBy(() -> userService.update(u2.getId(), upd))
//                .isInstanceOf(ResponseStatusException.class)
//                .hasMessageContaining(String.valueOf(HttpStatus.CONFLICT.value()));
//    }
//
//    @Test
//    void delete_notFound() {
//        assertThatThrownBy(() -> userService.delete(123456L))
//                .isInstanceOf(ResponseStatusException.class)
//                .hasMessageContaining(String.valueOf(HttpStatus.NOT_FOUND.value()));
//    }
//
//    private static UserDto dto(String name, String email, String role) {
//        UserDto d = new UserDto();
//        d.setName(name); d.setEmail(email); d.setRole(role);
//        return d;
//    }
}
