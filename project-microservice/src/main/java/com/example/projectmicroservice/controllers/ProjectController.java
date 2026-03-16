package com.example.projectmicroservice.controllers;
import com.example.projectmicroservice.models.Project;
import com.example.projectmicroservice.services.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/projects")
    public String newProject(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("ownerId") UUID ownerId
    ) {
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setCreatedAt(LocalDateTime.now());

        if (ownerId == null) {
            ownerId = UUID.randomUUID();
        }
        project.setOwnerId(ownerId);

        projectService.saveProject(project);

        return "redirect:http://localhost:8080/document-microservice/documents?userId="
                + ownerId + "&name=" + name;
    }
}

