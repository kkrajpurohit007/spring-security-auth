package org.finora.app.auth.dto;

public record AuthResponse(
        String token,
        UserResponse user) {
}
