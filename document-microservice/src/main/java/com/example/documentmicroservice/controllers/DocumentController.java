package com.example.documentmicroservice.controllers;
import com.example.documentmicroservice.models.Document;
import com.example.documentmicroservice.models.File;
import com.example.documentmicroservice.models.Version;
import com.example.documentmicroservice.repositories.DocumentRepository;
import com.example.documentmicroservice.repositories.FileRepository;
import com.example.documentmicroservice.repositories.VersionRepository;
import com.example.documentmicroservice.services.DocumentService;
import com.example.documentmicroservice.services.FileService;
import com.example.documentmicroservice.services.VersionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class DocumentController {
    private final DocumentService documentService;
    private final FileService fileService;
    private final VersionService versionService;

    public DocumentController(DocumentService documentService, FileService fileService, VersionService versionService) {
        this.documentService = documentService;
        this.fileService = fileService;
        this.versionService = versionService;
    }

    @GetMapping("/documents")
    public String showWorkspace(
            @RequestParam(value = "userId", required = false) UUID userId,
            @RequestParam(value = "name", required = false, defaultValue = "User") String name,
            Model model) {

        if (userId == null) {
            return "redirect:/error";
        }

        Map<String, Object> workspaceData = documentService.getWorkspaceData(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("username", name);
        model.addAttribute("groupedDocs", workspaceData.get("groupedDocs"));
        model.addAttribute("activeDocs", workspaceData.get("activeDocs"));
        model.addAttribute("draftDocs", workspaceData.get("draftDocs"));

        return "documents";
    }

    @PostMapping("/documents")
    public String uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") UUID userId,
            @RequestParam(value = "projectId", required = false) UUID projectId,
            @RequestParam(value = "name", required = false, defaultValue = "User") String name,
            Model model) {

            Document document = new Document();
            document.setName(file.getOriginalFilename());
            document.setDescription("Active");
            document.setCreatedBy(userId);
            document.setProjectId(projectId != null ? projectId : UUID.randomUUID());
            document = documentService.saveDocument(document);

            Version version = new Version();
            version.setVersionNumber(1);
            version.setMessage("Initial upload");
            version.setDocumentId(document.getId());
            version.setUserId(userId);
            version.setActive(true);
            version.setApproved(false);
            version = versionService.saveVersion(version);

        File fileEntity = new File();
        fileEntity.setFile_path(safeGetBytes(file));
        fileEntity.setContent(file.getContentType());
        fileEntity.setVersionId(version.getId());
        fileEntity.setHistoryTimestamp(LocalDateTime.now());

        fileService.saveFile(fileEntity);

            return "redirect:http://localhost:8080/document-microservice/documents?userId="
                    +userId +"&name"+ name;
        }

    private byte[] safeGetBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file bytes", e);
        }
    }
}