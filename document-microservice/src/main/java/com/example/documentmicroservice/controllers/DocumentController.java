package com.example.documentmicroservice.controllers;
import com.example.documentmicroservice.models.Document;
import com.example.documentmicroservice.services.DocumentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/documents")
    public String showWorkspace(
            @RequestParam UUID userId,
            @RequestParam String name,
            Model model) {

        Map<String, Object> workspaceData = documentService.getWorkspaceData(userId);

        model.addAttribute("username", name);
        model.addAttribute("groupedDocs", workspaceData.get("groupedDocs"));
        model.addAttribute("activeDocs", workspaceData.get("activeDocs"));
        model.addAttribute("draftDocs", workspaceData.get("draftDocs"));
        model.addAttribute("allDocs", workspaceData.get("activeDocs"));

        return "documents";
    }
}
