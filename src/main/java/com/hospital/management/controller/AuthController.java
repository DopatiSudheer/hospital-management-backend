package com.hospital.management.controller;

import com.hospital.management.dto.ApiResponse;
import com.hospital.management.dto.ChangePasswordRequest;
import com.hospital.management.dto.LoginRequest;
import com.hospital.management.dto.LoginResponse;
import com.hospital.management.dto.RegisterRequest;
import com.hospital.management.model.User;
import com.hospital.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        try {
            // Validate role
            if (request.getRole() == null || 
                (!request.getRole().equals("ADMIN") && 
                 !request.getRole().equals("DOCTOR") && 
                 !request.getRole().equals("PATIENT"))) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Invalid role. Must be ADMIN, DOCTOR, or PATIENT"));
            }

            // Check if email already exists
            if (userService.emailExists(request.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Email already exists"));
            }

            // Validate required fields
            if (request.getName() == null || request.getName().trim().isEmpty() ||
                request.getEmail() == null || request.getEmail().trim().isEmpty() ||
                request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Name, email, and password are required"));
            }

            // Register user
            User user = userService.registerUser(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
            );

            LoginResponse response = new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "User registered successfully", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        try {
            // Validate required fields
            if (request.getEmail() == null || request.getEmail().trim().isEmpty() ||
                request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Email and password are required"));
            }

            // Attempt login
            User user = userService.loginUser(request.getEmail(), request.getPassword());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Invalid email or password"));
            }
            
            if (user.getFirstLogin()) {
                return ResponseEntity.ok(
                        new ApiResponse(true, "CHANGE_PASSWORD_REQUIRED", null)
                );
            }

            LoginResponse response = new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
            );

            return ResponseEntity.ok(new ApiResponse(true, "Login successful", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Login failed: " + e.getMessage()));
        }
    }
    
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(
            @RequestBody ChangePasswordRequest request) {

        User user = userService.loginUser(request.getEmail(), request.getOldPassword());

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Old password incorrect"));
        }

        // Update password
        user.setPassword(request.getNewPassword());

        // Mark first login completed
        user.setFirstLogin(false);

        userService.updateUser(user); // we will create this next

        return ResponseEntity.ok(
                new ApiResponse(true, "Password changed successfully"));
    }
}

