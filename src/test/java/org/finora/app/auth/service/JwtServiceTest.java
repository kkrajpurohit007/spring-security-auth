package org.finora.app.auth.service;

import org.finora.app.auth.entity.Role;
import org.finora.app.auth.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    // 256-bit secret key (32 bytes) encoded in Base64
    // "ThisIsASecretKeyForTestingTheJwtServiceLogic12345" -> Base64
    private static final String SECRET_KEY = "VGhpc0lzQVNlY3JldEtleUZvclRlc3RpbmdUaGVKd3RTZXJ2aWNlTG9naWMxMjM0NQ==";
    private static final long EXPIRATION_MS = 1000 * 60 * 60; // 1 hour

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION_MS);
    }

    @Test
    void testGenerateToken_ShouldGenerateValidToken() {
        User user = User.builder()
                .id(123L)
                .email("test@example.com")
                .role(Role.USER)
                .build();

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        // Simple regex check for JWT format (header.payload.signature)
        assertTrue(token.matches("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_.+/=]*$"));
    }

    @Test
    void testExtractUserId_ShouldReturnCorrectId() {
        User user = User.builder()
                .id(999L)
                .email("user@example.com")
                .build();

        String token = jwtService.generateToken(user);
        Long extractedId = jwtService.extractUserId(token);

        assertEquals(999L, extractedId);
    }

    @Test
    void testIsTokenValid_ShouldReturnTrueForValidToken() {
        User user = User.builder()
                .id(1L)
                .email("valid@example.com")
                .build();

        String token = jwtService.generateToken(user);

        // Same user
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void testIsTokenValid_ShouldReturnFalseForDifferentUser() {
        User user1 = User.builder().id(1L).email("user1@example.com").build();
        User user2 = User.builder().id(2L).email("user2@example.com").build();

        String token = jwtService.generateToken(user1);

        // Validation against user2 should fail because IDs don't match
        assertFalse(jwtService.isTokenValid(token, user2));
    }

    @Test
    void testIsTokenExpired_ShouldReturnFalseForFreshToken() {
        User user = User.builder().id(10L).build();
        String token = jwtService.generateToken(user);

        // Re-using public isTokenValid to implicitly check expiration
        assertTrue(jwtService.isTokenValid(token, user));
    }
}
