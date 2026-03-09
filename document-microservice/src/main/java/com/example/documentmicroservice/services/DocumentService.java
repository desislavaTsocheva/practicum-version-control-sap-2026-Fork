package com.example.documentmicroservice.services;

import com.example.documentmicroservice.models.Document;
import com.example.documentmicroservice.repositories.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<Document> findAll() {
        return documentRepository.findAll();
    }
}
