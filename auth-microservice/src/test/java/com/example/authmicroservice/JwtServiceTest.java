package com.example.authmicroservice;

import com.example.authmicroservice.models.User;
import com.example.authmicroservice.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void shouldGenerateValidToken() {
        String username = "marin_dev";

        String token = jwtService.generateToken(username);

        assertNotNull(token);
        assertEquals(username, jwtService.extractUsername(token));
    }

    @Test
    void shouldValidateCorrectToken() {
        String username = "marin_dev";
        String token = jwtService.generateToken(username);

        assertTrue(jwtService.isTokenValid(token, username));
    }

    @Test
    void shouldInvalidateTokenWithWrongUsername() {
        String username = "marin_dev";
        String token = jwtService.generateToken(username);

        assertFalse(jwtService.isTokenValid(token, "wrong_user"));
    }

    @Test
    void shouldExtractRoleFromUserToken() {
        User user = new User();
        user.setUsername("admin_user");
        user.setRole("ADMIN");

        String token = jwtService.generateToken(user);

        String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));

        assertEquals("ADMIN", role);
    }
}
