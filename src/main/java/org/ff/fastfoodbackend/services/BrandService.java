package org.ff.fastfoodbackend.services;

import org.ff.fastfoodbackend.dto.BrandDto;
import org.ff.fastfoodbackend.entities.restaurant.Brand;
import org.ff.fastfoodbackend.repositories.BrandRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> getAll() {
        return brandRepository.findAll();
    }

    public Brand getById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found"));
    }

    public Brand create(BrandDto request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }
        String name = request.getName().trim();
        if (brandRepository.existsByName(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Brand name already exists");
        }
        Brand brand = Brand.builder()
                .name(name)
                .build();
        return brandRepository.save(brand);
    }

    public Brand update(Long id, BrandDto request) {
        Brand existing = brandRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found"));
        if (request.getName() != null && !request.getName().isBlank()) {
            String newName = request.getName().trim();
            if (!newName.equalsIgnoreCase(existing.getName()) && brandRepository.existsByName(newName)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Brand name already exists");
            }
            existing.setName(newName);
        }
        return brandRepository.save(existing);
    }

    public void delete(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found");
        }
        brandRepository.deleteById(id);
    }
}
