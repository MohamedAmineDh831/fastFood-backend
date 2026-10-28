package org.ff.fastfoodbackend.dto;

import lombok.Data;

@Data
public class FoodDto {
    private String name;
    // Food type: e.g., PIZZA, SANDWICH
    private String type;
    // Food sub-type: e.g., TUNA, HAM
    private String subType;
}
