package org.ff.fastfoodbackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ff.fastfoodbackend.dto.FoodDto;
import org.ff.fastfoodbackend.entities.food.Food;
import org.ff.fastfoodbackend.services.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.ff.fastfoodbackend.entities.user.UserRole.Constants.CUSTOMER;

@RestController
@RequestMapping("/api/foods")
@Tag(name = "Foods", description = "Food management endpoints")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping
    @PreAuthorize("hasRole('" + CUSTOMER + "')")
    @Operation(summary = "List foods", description = "Retrieve all foods")
    public List<Food> getAll() {
        return foodService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get food by id")
    public Food getById(@PathVariable Long id) {
        return foodService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Create food")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Food.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    })
    public ResponseEntity<Food> create(@RequestBody FoodDto request) {
        Food saved = foodService.create(request);
        return ResponseEntity.created(URI.create("/api/foods/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update food")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Food.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public Food update(@PathVariable Long id, @RequestBody FoodDto request) {
        return foodService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete food")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        foodService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
