// Create an AuthController for handling sign-up and sign-in operations
package com.imperionite.cp1.controllers;

import com.imperionite.cp1.entities.User;
import com.imperionite.cp1.securities.JwtTokenProvider;
import com.imperionite.cp1.services.AuthService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Base path for auth routes

public class AuthController {
    @Autowired
    private AuthService authService; // Inject Auth service

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // Inject JWT provider

    @PostMapping("/register") // Endpoint for user registration

    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        authService.register(user); // Register user
        return ResponseEntity.ok("User registered successfully!"); // Success response
    }

    @PostMapping("/login") // Endpoint for user login
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        User authenticatedUser = authService.login(user); // Authenticate user
        if (authenticatedUser != null) {
            String jwt = jwtTokenProvider.generateToken(authenticatedUser.getUsername()); // Generate JWT

            // Create response body with JWT token
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("access", jwt); // Add access token to the response
            return ResponseEntity.ok(responseBody); // Return map containing JWT
        }

        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials!")); // Invalid credentials
                                                                                        
    }

}