package org.ff.fastfoodbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.ff.fastfoodbackend.entities.user.*;

@Data
public class RegisterUserDto {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Role is required")
    private UserRole role;

    public User toEntity() {

        return switch (this.getRole()) {
            case CUSTOMER -> Customer.builder()
                    .address(this.getAddress())
                    .build();

            case DELIVERY_PERSON -> DeliveryPerson.builder()
                    .address(this.getAddress())
                    .build();

            case RESTAURANT_MANAGER -> RestaurantManager.builder()
                    .address(this.getAddress())
                    .build();

            case ADMIN -> Admin.builder()
                    .address(this.getAddress())
                    .build();

        };
    }

}
