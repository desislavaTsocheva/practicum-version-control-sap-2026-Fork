package com.example.documentmicroservice.controllers;
import com.example.documentmicroservice.models.Document;
import com.example.documentmicroservice.models.File;
import com.example.documentmicroservice.models.Version;
import com.example.documentmicroservice.services.DocumentService;
import com.example.documentmicroservice.services.FileService;
import com.example.documentmicroservice.services.VersionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class DocumentController {
    private final DocumentService documentService;
    private final FileService fileService;
    private final VersionService versionService;

    private final RestTemplate restTemplate = new RestTemplate();

    public DocumentController(DocumentService documentService, FileService fileService, VersionService versionService) {
        this.documentService = documentService;
        this.fileService = fileService;
        this.versionService = versionService;
    }

    @GetMapping("/documents")
    public String showWorkspace(
            @RequestParam(value = "userId", required = false) UUID userId,
            @RequestParam(value = "name", required = false, defaultValue = "User") String name,
            @RequestParam(value = "pfp", required = false) String profilePicture,
            Model model) {

        if (userId == null) {
            return "redirect:/error";
        }

        Map<String, Object> workspaceData = documentService.getWorkspaceData(userId);

        @SuppressWarnings("unchecked")
        Map<String, Object> groupedDocs = (Map<String, Object>) workspaceData.getOrDefault("groupedDocs", new java.util.HashMap<>());
        List<Map<String, Object>> allProjects = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8080/project-microservice/projects/user/" + userId;

            // В showWorkspace метода на DocumentController:

            allProjects = restTemplate.getForObject(url, List.class);
            if (allProjects != null) {
                // ДОБАВИ ТОЗИ РЕД:
                model.addAttribute("allProjects", allProjects);

                for (Map<String, Object> proj : allProjects) {
                    String projectName = (String) proj.get("name");
                    if (!groupedDocs.containsKey(projectName)) {
                        groupedDocs.put(projectName, new java.util.ArrayList<>());
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Could not fetch projects: " + e.getMessage());
        }

        model.addAttribute("userId", userId);
        model.addAttribute("username", name);
        model.addAttribute("groupedDocs", groupedDocs);
        model.addAttribute("activeDocs", workspaceData.get("activeDocs"));
        model.addAttribute("draftDocs", workspaceData.get("draftDocs"));
        model.addAttribute("allProjects", allProjects); // Списъкът, който вземаш от Project Microservice
        return "documents";
    }

    @PostMapping("/documents")
    public String uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") UUID userId,
            @RequestParam("projectId") UUID projectId,
            @RequestParam(value = "projectName", required = false, defaultValue = "General") String projectName,
            @RequestParam(value = "name", required = false, defaultValue = "User") String name) {

        documentService.saveDocument(file,projectId,userId);

//        Version version = new Version();
//        version.setVersionNumber(1);
//        version.setMessage("Initial upload");
//        version.setDocumentId(.getId());
//        version.setUserId(userId);
//        version.setActive(true);
//        version.setApproved(false);
//        version = versionService.saveVersion(version);
//
//        File fileEntity = new File();
//        fileEntity.setFile_path(safeGetBytes(file));
//        fileEntity.setContent(file.getContentType());
//        fileEntity.setVersionId(version.getId());
//        fileEntity.setHistoryTimestamp(LocalDateTime.now());
//        fileService.saveFile(fileEntity);

        return "redirect:http://localhost:8080/document-microservice/documents?userId="
                + userId + "&name=" + name +"&projectId=" + projectId;
    }

    private byte[] safeGetBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file bytes", e);
        }
    }
}