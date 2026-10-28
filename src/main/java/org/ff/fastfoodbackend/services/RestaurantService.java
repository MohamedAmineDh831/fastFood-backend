package org.ff.fastfoodbackend.services;

import org.ff.fastfoodbackend.dto.RestaurantDto;
import org.ff.fastfoodbackend.entities.restaurant.Brand;
import org.ff.fastfoodbackend.entities.restaurant.Restaurant;
import org.ff.fastfoodbackend.entities.user.User;
import org.ff.fastfoodbackend.repositories.BrandRepository;
import org.ff.fastfoodbackend.repositories.RestaurantRepository;
import org.ff.fastfoodbackend.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository, BrandRepository brandRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.brandRepository = brandRepository;
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    public Restaurant getById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
    }

    public Restaurant create(RestaurantDto request) {
        if (request.getAddress() == null || request.getAddress().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address is required");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        if (request.getManagerId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "managerId is required");
        }
        if (request.getBrandId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "brandId is required");
        }
        if (restaurantRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager user not found"));
//        if (manager.getRole() != UserRole.RESTAURANT_MANAGER) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Manager must have role RESTAURANT_MANAGER");
//        }
        restaurantRepository.findByManagerId(manager.getId()).ifPresent(r -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This manager is already assigned to another restaurant");
        });

        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found"));

        Restaurant restaurant = Restaurant.builder()
                .address(request.getAddress().trim())
                .email(request.getEmail().trim())
                .manager(manager)
                .brand(brand)
                .build();
        return restaurantRepository.save(restaurant);
    }

    public Restaurant update(Long id, RestaurantDto request) {
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));

        if (request.getAddress() != null && !request.getAddress().isBlank()) {
            existing.setAddress(request.getAddress().trim());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            String newEmail = request.getEmail().trim();
            if (!newEmail.equalsIgnoreCase(existing.getEmail()) && restaurantRepository.existsByEmail(newEmail)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
            }
            existing.setEmail(newEmail);
        }
        if (request.getManagerId() != null) {
            User manager = userRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager user not found"));
//            if (manager.getRole() != UserRole.RESTAURANT_MANAGER) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Manager must have role RESTAURANT_MANAGER");
//            }
            restaurantRepository.findByManagerId(manager.getId()).ifPresent(other -> {
                if (!other.getId().equals(existing.getId())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "This manager is already assigned to another restaurant");
                }
            });
            existing.setManager(manager);
        }
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found"));
            existing.setBrand(brand);
        }
        return restaurantRepository.save(existing);
    }

    public void delete(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
        }
        restaurantRepository.deleteById(id);
    }
}
