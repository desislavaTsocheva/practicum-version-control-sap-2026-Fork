package com.example.projectmicroservice;

import com.example.projectmicroservice.models.Project;
import com.example.projectmicroservice.repositories.ProjectRepository;
import com.example.projectmicroservice.services.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void getAllProjects_ShouldReturnUserProjects() {
        UUID userId = UUID.randomUUID();
        Project project = new Project();
        project.setOwnerId(userId);

        when(projectRepository.findByOwnerId(userId)).thenReturn(List.of(project));

        List<Project> result = projectService.getAllProjects(userId);

        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getOwnerId());
        verify(projectRepository).findByOwnerId(userId);
    }

    @Test
    void getProjects_ShouldReturnAllProjects() {
        when(projectRepository.findAll()).thenReturn(List.of(new Project(), new Project()));

        List<Project> result = projectService.getProjects();

        assertEquals(2, result.size());
    }

    @Test
    void saveProject_ShouldCallRepository() {
        Project project = new Project();

        projectService.saveProject(project);

        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void countAllProjects_ShouldReturnNumber() {
        when(projectRepository.count()).thenReturn(10L);

        long count = projectService.countAllProjects();

        assertEquals(10L, count);
    }

    @Test
    void getProjectsForUser_ShouldReturnVisibleProjects() {
        UUID userId = UUID.randomUUID();
        when(projectRepository.findAllVisibleProjects(userId)).thenReturn(List.of(new Project()));

        List<Project> result = projectService.getProjectsForUser(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

}
