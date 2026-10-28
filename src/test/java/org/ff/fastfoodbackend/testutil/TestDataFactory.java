package org.ff.fastfoodbackend.testutil;

import org.ff.fastfoodbackend.entities.*;
import org.ff.fastfoodbackend.entities.food.Food;
import org.ff.fastfoodbackend.entities.food.FoodSubType;
import org.ff.fastfoodbackend.entities.food.FoodType;
import org.ff.fastfoodbackend.entities.restaurant.Brand;
import org.ff.fastfoodbackend.entities.restaurant.Restaurant;
import org.ff.fastfoodbackend.entities.user.User;
import org.ff.fastfoodbackend.entities.user.UserRole;
import org.ff.fastfoodbackend.repositories.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@Transactional
public class TestDataFactory {

    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;
    private final ProductRepository productRepository;

    public TestDataFactory(UserRepository userRepository,
                           BrandRepository brandRepository,
                           RestaurantRepository restaurantRepository,
                           FoodRepository foodRepository,
                           ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.brandRepository = brandRepository;
        this.restaurantRepository = restaurantRepository;
        this.foodRepository = foodRepository;
        this.productRepository = productRepository;
    }

    public User newUser(String name, String email, UserRole role) {
//        User u = User.builder().name(name).email(email).role(role).build();
        User u = null;
        return userRepository.saveAndFlush(u);
    }

    public User newManager(String name, String email) {
        return newUser(name, email, UserRole.RESTAURANT_MANAGER);
    }

    public Brand newBrand(String name) {
        Brand b = Brand.builder().name(name).build();
        return brandRepository.saveAndFlush(b);
    }

    public Restaurant newRestaurant(String address, String email, Brand brand, User manager) {
        Restaurant r = Restaurant.builder()
                .address(address)
                .email(email)
                .brand(brand)
                .manager(manager)
                .build();
        return restaurantRepository.saveAndFlush(r);
    }

    public Food newFood(String name, FoodType type, FoodSubType subType) {
        Food f = Food.builder()
                .name(name)
                .type(type)
                .subType(subType)
                .build();
        return foodRepository.saveAndFlush(f);
    }

    public Product newProduct(Restaurant restaurant, Food food, BigDecimal price, boolean available) {
        Product p = Product.builder()
                .restaurant(restaurant)
                .food(food)
                .price(price)
                .available(available)
                .build();
        return productRepository.saveAndFlush(p);
    }
}
