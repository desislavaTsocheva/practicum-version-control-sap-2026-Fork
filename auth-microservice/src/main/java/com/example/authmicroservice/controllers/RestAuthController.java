package com.example.authmicroservice.controllers;

import com.example.authmicroservice.dto.LoginRequest;
import com.example.authmicroservice.dto.UserDto;
import com.example.authmicroservice.models.User;
import com.example.authmicroservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-auth")
public class RestAuthController {
    private final UserService userService;

    public RestAuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute LoginRequest request) {
        String token = userService.authenticate(request.getUsername(), request.getPassword());
        if (token != null) {
            return ResponseEntity.ok(java.util.Map.of(
                    "message", "Login successful!",
                    "token", token
            ));
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

}