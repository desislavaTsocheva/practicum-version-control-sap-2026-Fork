package com.example.documentmicroservice.services;

import com.example.documentmicroservice.models.Document;
import com.example.documentmicroservice.repositories.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public Map<String, Object> getWorkspaceData(UUID userId) {
        List<Document> allDocs = documentRepository.findByCreatedBy(userId);
        Map<String, Object> data = new HashMap<>();

        Map<UUID, List<Document>> tree = allDocs.stream()
                .collect(Collectors.groupingBy(Document::getProjectId));
        data.put("groupedDocs", tree);

        List<Document> active = allDocs.stream()
                .filter(d -> "Active".equalsIgnoreCase(d.getDescription()))
                .collect(Collectors.toList());
        data.put("activeDocs", active);

        List<Document> drafts = allDocs.stream()
                .filter(d -> "Draft".equalsIgnoreCase(d.getDescription()))
                .collect(Collectors.toList());
        data.put("draftDocs", drafts);

        return data;
    }

    @Transactional
    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }
}
