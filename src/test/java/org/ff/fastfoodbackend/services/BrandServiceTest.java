package org.ff.fastfoodbackend.services;

import org.ff.fastfoodbackend.dto.BrandDto;
import org.ff.fastfoodbackend.entities.restaurant.Brand;
import org.ff.fastfoodbackend.repositories.BrandRepository;
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
class BrandServiceTest {

    @Autowired private BrandService brandService;
    @Autowired private BrandRepository brandRepository;

    @BeforeEach
    void clean() {
        brandRepository.deleteAll();
    }

    @Test
    void create_update_delete_and_conflicts() {
        BrandDto dto = new BrandDto();
        dto.setName(" FastPizza ");
        Brand b = brandService.create(dto);
        assertThat(b.getId()).isNotNull();
        assertThat(b.getName()).isEqualTo("FastPizza");

        // Duplicate
        BrandDto dup = new BrandDto(); dup.setName("FastPizza");
        assertThatThrownBy(() -> brandService.create(dup))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(String.valueOf(HttpStatus.CONFLICT.value()));

        // Update to new unique name
        BrandDto upd = new BrandDto(); upd.setName("FastPizzaX");
        Brand updated = brandService.update(b.getId(), upd);
        assertThat(updated.getName()).isEqualTo("FastPizzaX");

        // Update conflict against existing name
        Brand other = brandService.create(name("Other"));
        assertThatThrownBy(() -> brandService.update(b.getId(), name("Other")))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(String.valueOf(HttpStatus.CONFLICT.value()));

        // Delete and 404 after
        brandService.delete(other.getId());
        assertThatThrownBy(() -> brandService.getById(other.getId()))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(String.valueOf(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void badRequest_on_missing_name() {
        BrandDto empty = new BrandDto();
        assertThatThrownBy(() -> brandService.create(empty))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(String.valueOf(HttpStatus.BAD_REQUEST.value()));
    }

    private static BrandDto name(String n) {
        BrandDto d = new BrandDto(); d.setName(n); return d;
        }
}
