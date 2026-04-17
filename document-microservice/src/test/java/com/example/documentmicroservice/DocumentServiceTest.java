package com.example.documentmicroservice;

import com.example.documentmicroservice.models.Document;
import com.example.documentmicroservice.repositories.DocumentRepository;
import com.example.documentmicroservice.services.DocumentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentService documentService;

    @Test
    void getWorkspaceData_ShouldGroupAndFilterCorrectly() {
        UUID userId = UUID.randomUUID();
        Document doc1 = new Document();
        doc1.setDescription("Active");
        Document doc2 = new Document();
        doc2.setDescription("Draft");

        when(documentRepository.findByCreatedBy(userId)).thenReturn(List.of(doc1, doc2));

        Map<String, Object> result = documentService.getWorkspaceData(userId);

        assertNotNull(result);
        assertTrue(result.containsKey("groupedDocs"));
        assertEquals(1, ((List<?>) result.get("activeDocs")).size());
        assertEquals(1, ((List<?>) result.get("draftDocs")).size());
    }

    @Test
    void saveDocument_ShouldCreateValidDocument() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test.pdf");

        UUID projectId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(documentRepository.save(any(Document.class))).thenAnswer(i -> i.getArgument(0));

        Document saved = documentService.saveDocument(mockFile, projectId, "ProjectName", userId);

        assertEquals("test.pdf", saved.getName());
        assertEquals(userId, saved.getCreatedBy());
        verify(documentRepository, times(1)).save(any(Document.class));
    }

    @Test
    void findById_ShouldThrowExceptionIfNotFound() {
        UUID id = UUID.randomUUID();
        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> documentService.findById(id));
    }
}
