package com.example.authmicroservice.services;

import com.example.authmicroservice.models.User;
import com.example.authmicroservice.repositories.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Тази анотация автоматично прави конструктор за userRepository
public class AuthService {

    private final UserRepository userRepository;

    // ТОВА Е МЕТОДЪТ, КОЙТО ТЕСТВАМЕ
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}