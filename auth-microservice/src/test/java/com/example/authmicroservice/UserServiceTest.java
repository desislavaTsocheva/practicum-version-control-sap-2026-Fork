package com.example.authmicroservice;

import com.example.authmicroservice.models.User;
import com.example.authmicroservice.repositories.UserRepository;
import com.example.authmicroservice.services.JwtService;
import com.example.authmicroservice.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void authenticate_ShouldReturnToken_WhenCredentialsAreValid() {
        User user = new User();
        user.setUsername("kris");
        user.setPassword("hashed_pass");

        when(userRepository.findByUsername("kris")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("raw_pass", "hashed_pass")).thenReturn(true);
        when(jwtService.generateToken("kris")).thenReturn("mocked-jwt-token");

        String token = userService.authenticate("kris", "raw_pass");

        assertEquals("mocked-jwt-token", token);
    }

    @Test
    void isPasswordValid_ShouldReturnTrue_ForStrongPassword() {
        assertTrue(userService.isPasswordValid("Pass1"));
        assertFalse(userService.isPasswordValid("OnlyLetters"));
        assertFalse(userService.isPasswordValid("PASS4"));
    }

    @Test
    void findById_ShouldReturnNull_WhenUserDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        User result = userService.findById(id);

        assertNull(result);
    }
}