package com.example.authmicroservice.controllers;

import com.example.authmicroservice.dto.LoginRequest;
import com.example.authmicroservice.dto.UserDto;
import com.example.authmicroservice.models.User;
import com.example.authmicroservice.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String viewRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String register(@Valid @ModelAttribute("user") UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        boolean isUsernameAvailable = userService.isUsernameAvailable(userDto.getUsername());
        boolean isEmailAvailable = userService.isEmailAvailable(userDto.getEmail());

        if (!isUsernameAvailable) {
            bindingResult.rejectValue("username", "error.user", "Username is already in use");
        }
        if (!isEmailAvailable) {
            bindingResult.rejectValue("email", "error.user", "Email is already in use");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.addUser(userDto);

        return "redirect:./";
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