package org.finora.app.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.finora.app.auth.dto.AuthResponse;
import org.finora.app.auth.dto.CreateUserRequest;
import org.finora.app.auth.dto.LoginRequest;
import org.finora.app.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody CreateUserRequest request) {
        log.info("Received signup request for email: {}", request.email());
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Received login request for email: {}", request.email());
        return ResponseEntity.ok(authService.login(request));
    }
}
