package org.ff.fastfoodbackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ff.fastfoodbackend.dto.BrandDto;
import org.ff.fastfoodbackend.entities.restaurant.Brand;
import org.ff.fastfoodbackend.services.BrandService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.ff.fastfoodbackend.entities.user.UserRole.Constants.RESTAURANT_MANAGER;

@RestController
@RequestMapping("/api/brands")
@Tag(name = "Brands", description = "Brand management endpoints")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    @Operation(summary = "List brands", description = "Retrieve all brands")
    public List<Brand> getAll() {
        return brandService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get brand by id")
    public Brand getById(@PathVariable Long id) {
        return brandService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Create brand")
    @PreAuthorize("hasRole('" + RESTAURANT_MANAGER + "')")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Brand.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content)
    })
    public ResponseEntity<Brand> create(@RequestBody BrandDto request) {
        Brand saved = brandService.create(request);
        return ResponseEntity.created(URI.create("/api/brands/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update brand")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Brand.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content)
    })
    public Brand update(@PathVariable Long id, @RequestBody BrandDto request) {
        return brandService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete brand")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        brandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
