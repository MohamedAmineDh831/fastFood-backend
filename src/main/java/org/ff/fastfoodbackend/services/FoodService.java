package org.ff.fastfoodbackend.services;

import org.ff.fastfoodbackend.dto.FoodDto;
import org.ff.fastfoodbackend.entities.food.Food;
import org.ff.fastfoodbackend.entities.food.FoodSubType;
import org.ff.fastfoodbackend.entities.food.FoodType;
import org.ff.fastfoodbackend.repositories.FoodRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<Food> getAll() {
        return foodRepository.findAll();
    }

    public Food getById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));
    }

    public Food create(FoodDto request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }
        if (request.getType() == null || request.getType().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type is required (PIZZA, SANDWICH)");
        }
        if (request.getSubType() == null || request.getSubType().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sub-type is required (TUNA, HAM)");
        }

        FoodType type = parseType(request.getType());
        FoodSubType subType = parseSubType(request.getSubType());

        Food food = Food.builder()
                .name(request.getName().trim())
                .type(type)
                .subType(subType)
                .build();
        return foodRepository.save(food);
    }

    public Food update(Long id, FoodDto request) {
        Food existing = foodRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            existing.setName(request.getName().trim());
        }
        if (request.getType() != null && !request.getType().isBlank()) {
            existing.setType(parseType(request.getType()));
        }
        if (request.getSubType() != null && !request.getSubType().isBlank()) {
            existing.setSubType(parseSubType(request.getSubType()));
        }
        return foodRepository.save(existing);
    }

    public void delete(Long id) {
        if (!foodRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found");
        }
        foodRepository.deleteById(id);
    }

    private FoodType parseType(String typeStr) {
        try {
            return FoodType.valueOf(typeStr.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid type. Allowed: PIZZA, SANDWICH");
        }
    }

    private FoodSubType parseSubType(String subTypeStr) {
        try {
            return FoodSubType.valueOf(subTypeStr.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sub-type. Allowed: TUNA, HAM");
        }
    }
}
