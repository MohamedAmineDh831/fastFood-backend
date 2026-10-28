package org.ff.fastfoodbackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ff.fastfoodbackend.dto.ProductDto;
import org.ff.fastfoodbackend.entities.Product;
import org.ff.fastfoodbackend.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/products")
@Tag(name = "Products", description = "Restaurant products (Food + price + availability)")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "List products for a restaurant")
    public List<Product> list(@PathVariable Long restaurantId) {
        return productService.listByRestaurant(restaurantId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id for a restaurant")
    public Product get(@PathVariable Long restaurantId, @PathVariable("id") Long productId) {
        return productService.getById(restaurantId, productId);
    }

    @PostMapping
    @Operation(summary = "Create product for a restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content)
    })
    public ResponseEntity<Product> create(@PathVariable Long restaurantId, @RequestBody ProductDto dto) {
        Product saved = productService.create(restaurantId, dto);
        return ResponseEntity.created(URI.create("/api/restaurants/" + restaurantId + "/products/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product (price, availability, food)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content)
    })
    public Product update(@PathVariable Long restaurantId, @PathVariable("id") Long productId, @RequestBody ProductDto dto) {
        return productService.update(restaurantId, productId, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<Void> delete(@PathVariable Long restaurantId, @PathVariable("id") Long productId) {
        productService.delete(restaurantId, productId);
        return ResponseEntity.noContent().build();
    }
}
