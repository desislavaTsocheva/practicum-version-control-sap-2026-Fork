package com.example.documentmicroservice.controllers;
import com.example.documentmicroservice.models.File;
import com.example.documentmicroservice.models.Version;
import com.example.documentmicroservice.services.DocumentService;
import com.example.documentmicroservice.services.FileService;
import com.example.documentmicroservice.services.VersionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.example.documentmicroservice.models.Document;
import java.io.IOException;
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
    public String showWorkspace(@RequestParam(value = "userId", required = false) UUID userId, @RequestParam(value = "name", required = false, defaultValue = "User") String name, @RequestParam(value = "pfp", required = false) String profilePicture, Model model) {

        if (userId == null) {
            return "redirect:/error";
        }

        Map<String, Object> workspaceData = documentService.getWorkspaceData(userId);

        @SuppressWarnings("unchecked") Map<String, Object> groupedDocs = (Map<String, Object>) workspaceData.getOrDefault("groupedDocs", new java.util.HashMap<>());
        List<Map<String, Object>> allProjects = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8080/project-microservice/projects/user/" + userId;

            allProjects = restTemplate.getForObject(url, List.class);
            if (allProjects != null) {
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

        String role = "user";
        try {
            RestTemplate restTemplate = new RestTemplate();
            String authUrl = "http://localhost:8080/auth-microservice/users/" + userId;
            Map<String, Object> userResponse = restTemplate.getForObject(authUrl, Map.class);

            if (userResponse != null && userResponse.get("role") != null) {
                role = (String) userResponse.get("role");
            }
        } catch (Exception e) {
            System.err.println("Could not fetch user role: " + e.getMessage());
        }
        model.addAttribute("userRole", role.toLowerCase());
        model.addAttribute("userId", userId);
        model.addAttribute("username", name);
        model.addAttribute("groupedDocs", groupedDocs);
        model.addAttribute("activeDocs", workspaceData.get("activeDocs"));
        model.addAttribute("draftDocs", workspaceData.get("draftDocs"));
        model.addAttribute("allProjects", allProjects);
        return "documents";
    }

    @GetMapping("/documents/all")
    @ResponseBody
    public List<Document> getAllDocuments() {
        return documentService.findAll();
    }

    @GetMapping("/documents/count")
    @ResponseBody
    public long countDocuments() {
        return documentService.countAllDocuments();
    }

    @PostMapping("/documents")
    public String uploadDocument(@RequestParam("file") MultipartFile file, @RequestParam("userId") UUID userId, @RequestParam("projectId") UUID projectId, @RequestParam(value = "projectName", required = false) String projectName, @RequestParam(value = "name", required = false, defaultValue = "User") String name, Model model) {
        Document document = (Document) documentService.saveDocument(file, projectId, projectName, userId);
        Version version = versionService.saveVersion(userId, document);
        File fileSave = fileService.saveFile(version.getId(), file);
        return "redirect:http://localhost:8080/document-microservice/documents?userId=" + userId + "&name=" + name;
    }

    private byte[] safeGetBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file bytes", e);
        }
    }

    @GetMapping("/documents/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable UUID id) {
        File fileEntity = fileService.getFileEntityByDocumentId(id);
        Document doc = documentService.findById(id);
        byte[] data = fileEntity.getFile_path();
        String contentType = fileEntity.getContent();

        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getName() + "\"").contentType(MediaType.parseMediaType(contentType)).body(data);
    }
}