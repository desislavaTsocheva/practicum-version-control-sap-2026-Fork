package com.example.authmicroservice.controllers;

import com.example.authmicroservice.models.User;
import com.example.authmicroservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RestTemplate restTemplate;

    public AdminController(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        if (principal == null) {
            System.out.println("No principal found! Redirecting to login...");
            return "redirect:/";
        }

        try {
            String username = principal.getName();
            User currentAdmin = userService.findByUsername(username);

            if (currentAdmin == null || !"admin".equals(currentAdmin.getRole())) {
                return "redirect:/";
            }

            List<User> allUsers = userService.getUsers();
            int projectCount = 0;
            int documentCount = 0;
            try {
                String response = restTemplate.getForObject("http://localhost:8080/project-microservice/projects/count", String.class);
                String responseDoc = restTemplate.getForObject("http://localhost:8080/document-microservice/documents/count", String.class);
                System.out.println("DEBUG: Response from Project & Document Service is: " + response + " " + responseDoc);

                if (response != null) {
                    projectCount = Integer.parseInt(response.trim());
                }
                if (responseDoc != null) {
                    documentCount = Integer.parseInt(responseDoc.trim());
                }
            } catch (Exception e) {
                System.out.println("Could not connect to Project Service: " + e.getMessage());
                System.out.println("Could not connect to Document Service: " + e.getMessage());
            }

            model.addAttribute("users", allUsers);
            model.addAttribute("totalUsers", allUsers.size());
            model.addAttribute("projects", projectCount);
            model.addAttribute("documents", documentCount);
            model.addAttribute("admin", currentAdmin.getId());

            return "adminDashboard";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }

    }

    @DeleteMapping("/admin/users/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        try {
            boolean deleted = userService.deleteUser(id);

            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
