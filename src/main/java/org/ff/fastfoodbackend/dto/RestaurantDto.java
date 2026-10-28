package org.ff.fastfoodbackend.dto;

import lombok.Data;

@Data
public class RestaurantDto {
    private String address;
    private String email;
    // The manager user id for this restaurant (required on create)
    private Long managerId;
    // The brand id this restaurant belongs to (required on create)
    private Long brandId;
}
