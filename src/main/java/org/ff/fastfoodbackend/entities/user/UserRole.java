package org.ff.fastfoodbackend.entities.user;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN(Constants.ADMIN),
    DELIVERY_PERSON(Constants.DELIVERY_PERSON),
    RESTAURANT_MANAGER(Constants.RESTAURANT_MANAGER),
    CUSTOMER(Constants.CUSTOMER);

    private final String securityName;

    UserRole(String securityName) {
        this.securityName = securityName;
    }

    // Ces chaînes sont des "vray" constantes pour Java
    public static class Constants {
        public static final String ADMIN = "admin";
        public static final String DELIVERY_PERSON = "delivery_person";
        public static final String RESTAURANT_MANAGER = "restaurant_manager";
        public static final String CUSTOMER = "customer";
    }
}