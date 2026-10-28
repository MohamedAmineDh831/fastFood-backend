package org.ff.fastfoodbackend.services;

import org.ff.fastfoodbackend.dto.ProductDto;
import org.ff.fastfoodbackend.entities.food.Food;
import org.ff.fastfoodbackend.entities.Product;
import org.ff.fastfoodbackend.entities.restaurant.Restaurant;
import org.ff.fastfoodbackend.repositories.FoodRepository;
import org.ff.fastfoodbackend.repositories.ProductRepository;
import org.ff.fastfoodbackend.repositories.RestaurantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;

    public ProductService(ProductRepository productRepository,
                          RestaurantRepository restaurantRepository,
                          FoodRepository foodRepository) {
        this.productRepository = productRepository;
        this.restaurantRepository = restaurantRepository;
        this.foodRepository = foodRepository;
    }

    public List<Product> listByRestaurant(Long restaurantId) {
        ensureRestaurantExists(restaurantId);
        return productRepository.findByRestaurantId(restaurantId);
    }

    public Product getById(Long restaurantId, Long productId) {
        ensureRestaurantExists(restaurantId);
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        if (!p.getRestaurant().getId().equals(restaurantId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found in this restaurant");
        }
        return p;
    }

    public Product create(Long restaurantId, ProductDto dto) {
        if (dto.getFoodId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "foodId is required");
        }
        if (dto.getPrice() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "price is required");
        }
        validatePrice(dto.getPrice());

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
        Food food = foodRepository.findById(dto.getFoodId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));

        if (productRepository.existsByRestaurantIdAndFoodId(restaurantId, food.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product for this food already exists in this restaurant");
        }

        boolean available = dto.getAvailable() == null ? true : dto.getAvailable();

        Product product = Product.builder()
                .restaurant(restaurant)
                .food(food)
                .price(dto.getPrice())
                .available(available)
                .build();
        return productRepository.save(product);
    }

    public Product update(Long restaurantId, Long productId, ProductDto dto) {
        Product existing = getById(restaurantId, productId);
        if (dto.getPrice() != null) {
            validatePrice(dto.getPrice());
            existing.setPrice(dto.getPrice());
        }
        if (dto.getAvailable() != null) {
            existing.setAvailable(dto.getAvailable());
        }
        if (dto.getFoodId() != null && !dto.getFoodId().equals(existing.getFood().getId())) {
            Food newFood = foodRepository.findById(dto.getFoodId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));
            if (productRepository.existsByRestaurantIdAndFoodId(restaurantId, newFood.getId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Another product for this food already exists in this restaurant");
            }
            existing.setFood(newFood);
        }
        return productRepository.save(existing);
    }

    public void delete(Long restaurantId, Long productId) {
        Product existing = getById(restaurantId, productId);
        productRepository.deleteById(existing.getId());
    }

    private void ensureRestaurantExists(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price.scale() > 2 || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid price");
        }
    }
}
