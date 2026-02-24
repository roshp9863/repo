package org.connect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.*;
import org.connect.security.SecurityUtils;
import org.connect.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "User authentication endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "Register a new user")
    public ResponseEntity<Map<String, String>> signup(@Valid @RequestBody AuthSignupRequest request) {
        log.info("Signup request for email: {}, role: {}", request.getEmail(), request.getRole());
        authService.signup(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Signup successful. Please check your email for verification.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> login(@Valid @RequestBody AuthLoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        AuthTokenResponse response = authService.login(request);
        return ResponseEntity.ok(new ApiResponse<>("Login successful", response));
    }

    @GetMapping("/verify-email")
    @Operation(summary = "Verify email with token")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok(new ApiResponse<>("Email verified successfully", null));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user info")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser() {
        UUID userId = SecurityUtils.getCurrentUserId();
        UserDTO user = authService.getCurrentUser(userId);
        return ResponseEntity.ok(new ApiResponse<>("User info retrieved", user));
    }

    @PostMapping("/dev/verify-all-pending")
    @Operation(summary = "DEV MODE: Auto-verify all pending users (Testing only)")
    public ResponseEntity<ApiResponse<String>> devVerifyAllPending() {
        String result = authService.devVerifyAllPendingUsers();
        return ResponseEntity.ok(new ApiResponse<>(result, null));
    }

}

