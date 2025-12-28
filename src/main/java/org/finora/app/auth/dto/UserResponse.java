package org.finora.app.auth.dto;

import org.finora.app.auth.entity.Role;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        Role role,
        LocalDateTime createdAt) {
}
