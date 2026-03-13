package com.example.authmicroservice.controllers;

import com.example.authmicroservice.dto.LoginRequest;
import com.example.authmicroservice.dto.UserDto;
import com.example.authmicroservice.models.User;
import com.example.authmicroservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
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
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String register(@Valid @ModelAttribute("user") UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        boolean isUsernameAvailable = userService.isUsernameAvailable(userDto.getUsername());
        boolean isEmailAvailable = userService.isEmailAvailable(userDto.getEmail());
        boolean isPasswordValid = userService.isPasswordValid(userDto.getPassword());

        if (!isUsernameAvailable) {
            bindingResult.rejectValue("username", "error.user", "Username is already in use");
        }
        if (!isEmailAvailable) {
            bindingResult.rejectValue("email", "error.user", "Email is already in use");
        }

        if(!isPasswordValid) {
            bindingResult.rejectValue("password", "error.password", "Use letters (A-z) and numbers " +
                    "(min. 6 chars)");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.addUser(userDto);

        return "redirect:./";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest request) {
        String token = userService.authenticate(request.getUsername(), request.getPassword());
        if (token != null) {
            User user = userService.findByUsername(request.getUsername());
            return "redirect:http://localhost:8080/document-microservice/documents?userId="
                    + user.getId() + "&name=" + user.getUsername();
        } else {
            return "redirect:/login?error=true";
        }
    }
}