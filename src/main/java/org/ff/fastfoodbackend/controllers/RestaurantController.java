package org.ff.fastfoodbackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ff.fastfoodbackend.dto.RestaurantDto;
import org.ff.fastfoodbackend.entities.restaurant.Restaurant;
import org.ff.fastfoodbackend.services.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurants", description = "Restaurant management endpoints")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    @Operation(summary = "List restaurants", description = "Retrieve all restaurants")
    public List<Restaurant> getAll() {
        return restaurantService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant by id")
    public Restaurant getById(@PathVariable Long id) {
        return restaurantService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Create restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Restaurant.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content)
    })
    public ResponseEntity<Restaurant> create(@RequestBody RestaurantDto request) {
        Restaurant saved = restaurantService.create(request);
        return ResponseEntity.created(URI.create("/api/restaurants/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Restaurant.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content)
    })
    public Restaurant update(@PathVariable Long id, @RequestBody RestaurantDto request) {
        return restaurantService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
