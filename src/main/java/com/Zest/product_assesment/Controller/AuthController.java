package com.Zest.product_assesment.Controller;

import com.Zest.product_assesment.Service.AuthService;
import com.Zest.product_assesment.dto.AuthRequestDTO;
import com.Zest.product_assesment.dto.JwtResponseDTO;
import com.Zest.product_assesment.dto.RefreshTokenRequestDTO;
import com.Zest.product_assesment.dto.RegisterRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Endpoints for user registration, authentication, and token management[cite: 10]")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register user", description = "Registers a new user with a specified or default role[cite: 10]")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        String assignedRole = authService.registerUser(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully with role: " + assignedRole);
    }

    @Operation(summary = "Authenticate user", description = "Validates credentials and returns JWT access and refresh tokens[cite: 10]")
    @ApiResponse(responseCode = "200", description = "Authentication successful")
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
        JwtResponseDTO response = authService.login(authRequestDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Refresh token", description = "Generates a new access token using a valid refresh token[cite: 10]")
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully")
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO requestDTO) {
        JwtResponseDTO response = authService.refreshToken(requestDTO);
        return ResponseEntity.ok(response);
    }
}