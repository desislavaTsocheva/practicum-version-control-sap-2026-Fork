package com.example.authmicroservice.controllers;

import com.example.authmicroservice.models.User;
import com.example.authmicroservice.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Map;
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
        User user = userService.findById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (user.getProfilePicture() == null) {
            return ResponseEntity.notFound().build();
        }

        System.out.println("size of pfp: " + user.getProfilePicture().length + " bytes");
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

    @PostMapping("/users/profile/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@PathVariable UUID id, @RequestBody Map<String, String> updates) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setFirstName(updates.get("firstName"));
        user.setLastName(updates.get("lastName"));
        user.setEmail(updates.get("email"));

        userService.save(user);
        return ResponseEntity.ok().body("{\"message\": \"Success\"}");
    }
}
