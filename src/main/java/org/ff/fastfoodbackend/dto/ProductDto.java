package org.ff.fastfoodbackend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long foodId;
    private BigDecimal price;
    private Boolean available; // optional on create; default true if null
}
