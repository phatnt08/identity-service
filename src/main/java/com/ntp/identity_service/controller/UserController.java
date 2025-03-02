package com.ntp.identity_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.ntp.identity_service.dto.request.UserCreationRequest;
import com.ntp.identity_service.dto.request.UserUpdateRequest;
import com.ntp.identity_service.dto.response.ApiResponse;
import com.ntp.identity_service.dto.response.UserResponse;
import com.ntp.identity_service.service.UserService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController // Lombok annotation to create a REST controller
@RequestMapping("/users") // Map the controller to the /users endpoint
@RequiredArgsConstructor // Lombok annotation to generate a constructor with all final fields as
                         // arguments
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE) // Lombok annotation to make the final fields private
@Slf4j // Lombok annotation to inject a logger
public class UserController {

    // Injecting the UserService using constructor injection
    UserService userService;

    /**
     * Endpoint to create a new user.
     * 
     * @param request the user creation request containing user details
     * @return an ApiResponse containing the created user response
     */
    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    /**
     * Endpoint to get a list of all users.
     * 
     * @return an ApiResponse containing the list of user responses
     */
    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    /**
     * Endpoint to get a user by ID.
     * 
     * @param userId the ID of the user to retrieve
     * @return an ApiResponse containing the user response
     */
    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    /**
     * Endpoint to update a user by ID.
     * 
     * @param userId  the ID of the user to update
     * @param request the user update request containing updated user details
     * @return an ApiResponse containing the updated user response
     */
    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    /**
     * Endpoint to delete a user by ID.
     * 
     * @param userId the ID of the user to delete
     * @return an ApiResponse containing a success message
     */
    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .result("User deleted successfully")
                .build();
    }

    /**
     * Endpoint to get the current user's information.
     * 
     * @return
     */
    @GetMapping("/me")
    ApiResponse<UserResponse> getMe() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }
}
