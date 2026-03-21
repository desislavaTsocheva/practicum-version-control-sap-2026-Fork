package com.example.authmicroservice.controllers;

import com.example.authmicroservice.models.User;
import com.example.authmicroservice.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/users/profile-pic/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getProfilePic(@PathVariable UUID id) {
        System.out.println(">>> Търся снимка за потребител ID: " + id);
        User user = userService.findById(id);

        if (user == null) {
            System.out.println(">>> ГРЕШКА: Потребителят не съществува в базата!");
            return ResponseEntity.notFound().build();
        }

        if (user.getProfilePicture() == null) {
            System.out.println(">>> ГРЕШКА: Потребителят е намерен, но няма записана снимка!");
            return ResponseEntity.notFound().build();
        }

        System.out.println(">>> УСПЕХ: Снимката е намерена, размер: " + user.getProfilePicture().length + " bytes");
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(user.getProfilePicture());
    }
    @GetMapping("/users/profile/{id}")
    public String getUserProfile(@PathVariable UUID id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "profile-page";
    }
}
