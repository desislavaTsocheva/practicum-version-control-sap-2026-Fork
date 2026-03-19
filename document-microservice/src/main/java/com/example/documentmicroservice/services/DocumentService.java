package com.example.documentmicroservice.services;

import com.example.documentmicroservice.models.Document;
import com.example.documentmicroservice.repositories.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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

        // ПРОМЯНА: Групираме по Description (където пазим името на проекта)
        // вместо по ProjectId, за да се виждат в една и съща папка в UI
        Map<String, List<Document>> tree = allDocs.stream()
                .collect(Collectors.groupingBy(doc ->
                        doc.getDescription() != null ? doc.getDescription() : "General"
                ));

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
    public void saveDocument(MultipartFile file, UUID projectId, UUID userId) {
        Document doc = new Document();
        doc.setName(file.getOriginalFilename());
        doc.setProjectId(projectId);
        doc.setCreatedBy(userId); // Важно!
        doc.setCreatedAt(LocalDateTime.now());
        doc.setDescription("Active"); // Или каквото решиш
        documentRepository.save(doc);
    }
}
