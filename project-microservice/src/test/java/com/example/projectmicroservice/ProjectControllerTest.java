package com.example.projectmicroservice;

import com.example.projectmicroservice.controllers.ProjectController;
import com.example.projectmicroservice.models.Project;
import com.example.projectmicroservice.services.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public ProjectService projectService;

    @Test
    void addNewProject_ShouldSaveAndRedirect() throws Exception {
        UUID userId = UUID.randomUUID();
        String username = "Ivan";

        mockMvc.perform(post("/projects/add")
                        .param("projectName", "New Project")
                        .param("userId", userId.toString())
                        .param("username", username)
                        .param("description", "Test Description")
                        .param("isPublic", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:8080/document-microservice/documents?userId=" + userId + "&name=" + username));

        verify(projectService, times(1)).saveProject(any(Project.class));
    }

    @Test
    void getProjectsByUser_ShouldReturnJsonList() throws Exception {
        UUID userId = UUID.randomUUID();
        Project p = new Project();
        p.setName("My Project");

        when(projectService.getAllProjects(userId)).thenReturn(List.of(p));

        mockMvc.perform(get("/projects/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("My Project"));
    }

    @Test
    void countProjects_ShouldReturnNumber() throws Exception {
        when(projectService.countAllProjects()).thenReturn(15L);

        mockMvc.perform(get("/projects/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("15"));
    }

    @Test
    void getPublicProjects_ShouldReturnList() throws Exception {
        UUID userId = UUID.randomUUID();
        when(projectService.getProjectsForUser(userId)).thenReturn(List.of(new Project()));

        mockMvc.perform(get("/projects/public/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void createProject_ShouldRedirect() throws Exception {
        UUID ownerId = UUID.randomUUID();

        mockMvc.perform(post("/projects")
                        .param("ownerId", ownerId.toString())
                        .param("name", "Model Project"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:8080/document-microservice/documents?userId=" + ownerId));

        verify(projectService).saveProject(any(Project.class));
    }
}