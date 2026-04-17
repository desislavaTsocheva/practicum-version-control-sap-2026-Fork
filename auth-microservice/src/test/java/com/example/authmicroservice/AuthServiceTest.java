package com.example.authmicroservice;

import com.example.authmicroservice.repositories.UserRepository;
import com.example.authmicroservice.models.User;
import com.example.authmicroservice.services.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testFindUserByUsername_ShouldReturnUser() {
        User fakeUser = new User();
        fakeUser.setUsername("test_user");

        when(userRepository.findByUsername("test_user")).thenReturn(Optional.of(fakeUser));

        Optional<User> found = Optional.ofNullable(authService.loadUserByUsername("test_user"));

        assertTrue(found.isPresent());
        assertEquals("test_user", found.get().getUsername());

        verify(userRepository, times(1)).findByUsername("test_user");
    }

    @Test
    void testFindUserByUsername_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername("non_existent")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.loadUserByUsername("non_existent"));

        verify(userRepository, times(1)).findByUsername("non_existent");
    }


}
