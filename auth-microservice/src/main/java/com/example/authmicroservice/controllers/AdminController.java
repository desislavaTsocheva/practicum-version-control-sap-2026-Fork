package com.example.authmicroservice.controllers;

import com.example.authmicroservice.models.User;
import com.example.authmicroservice.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.List;

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

            Integer projects = 0;
            try {
                projects = restTemplate.getForObject("http://project-microservice/projects/count", Integer.class);
            } catch (Exception e) {
                System.out.println("Could not connect to Project Service, using 0");
            }

            model.addAttribute("users", allUsers);
            model.addAttribute("totalUsers", allUsers.size());
            model.addAttribute("projects", projects);
            model.addAttribute("admin", currentAdmin);

            return "adminDashboard";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }
}
