package org.ff.fastfoodbackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import org.ff.fastfoodbackend.dto.RegisterUserDto;
import org.ff.fastfoodbackend.entities.user.User;
import org.ff.fastfoodbackend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "List users", description = "Retrieve all users")
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping("/register")
    @PermitAll
    public ResponseEntity<String> register(@RequestBody RegisterUserDto registerUserDto) {
        userService.registerUser(registerUserDto);
        return ResponseEntity.ok("User registered successfully with role: " + registerUserDto.getRole());
    }
//
//    @PutMapping("/{id}")
//    @Operation(summary = "Update user")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "OK",
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
//            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
//            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
//            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content)
//    })
//    public User update(@PathVariable Long id, @RequestBody UserDto request) {
//        return userService.update(id, request);
//    }
//
//    @DeleteMapping("/{id}")
//    @Operation(summary = "Delete user")
//    @ApiResponses({
//            @ApiResponse(responseCode = "204", description = "No Content"),
//            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
//    })
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        userService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
}
