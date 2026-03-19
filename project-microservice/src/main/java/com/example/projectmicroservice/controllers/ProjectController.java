package com.example.projectmicroservice.controllers;
import com.example.projectmicroservice.models.Project;
import com.example.projectmicroservice.services.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/documents")
    public String newProject(
            @RequestParam("name") String projectName,
            @RequestParam("description") String description,
            @RequestParam("ownerId") UUID ownerId
    ) {
        Project project = new Project();
        project.setName(projectName);
        project.setDescription(description);
        project.setCreatedAt(LocalDateTime.now());

        if (ownerId == null) {
            ownerId = UUID.randomUUID();
        }
        project.setOwnerId(ownerId);

        projectService.saveProject(project);

        return "redirect:http://localhost:8080/document-microservice/documents?userId=" + ownerId;
    }

    @GetMapping("/projects/user/{userId}")
    @ResponseBody
    public List<Project> getProjectsByUser(@PathVariable UUID userId) {
        return projectService.getAllProjects(userId);
    }

    @PostMapping("/projects")
    public String createProject(@ModelAttribute Project project) {
        projectService.saveProject(project);
        return "redirect:http://localhost:8080/document-microservice/documents?userId=" + project.getOwnerId();
    }
}


