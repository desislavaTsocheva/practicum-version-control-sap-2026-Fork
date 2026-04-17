package com.example.documentmicroservice;

import com.example.documentmicroservice.controllers.DocumentController;
import com.example.documentmicroservice.models.Document;
import com.example.documentmicroservice.models.File;
import com.example.documentmicroservice.models.Version;
import com.example.documentmicroservice.services.DocumentService;
import com.example.documentmicroservice.services.FileService;
import com.example.documentmicroservice.services.VersionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DocumentController.class)
class DocumentControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public DocumentService documentService;

    @MockBean
    public FileService fileService;

    @MockBean
    public VersionService versionService;

    @Test
    void showWorkspace_ShouldReturnDocumentsView() throws Exception {
        UUID userId = UUID.randomUUID();
        when(documentService.getWorkspaceData(userId)).thenReturn(Map.of("activeDocs", List.of()));

        mockMvc.perform(get("/documents").param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("documents"))
                .andExpect(model().attributeExists("userRole", "groupedDocs"));
    }

    @Test
    void showWorkspace_ShouldRedirectIfUserIdMissing() throws Exception {
        mockMvc.perform(get("/documents"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    void downloadDocument_ShouldReturnFileBytes() throws Exception {
        UUID versionId = UUID.randomUUID();
        File mockFile = new File();
        mockFile.setFile_path("test content".getBytes());
        mockFile.setContent(MediaType.APPLICATION_PDF_VALUE);

        Version mockVersion = new Version();
        mockVersion.setMessage("test.pdf");

        when(fileService.getFileEntityByVersionId(versionId)).thenReturn(mockFile);
        when(versionService.findById(versionId)).thenReturn(mockVersion);

        mockMvc.perform(get("/documents/download/" + versionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF_VALUE))
                .andExpect(header().exists("Content-Disposition"));
    }

    @Test
    void getDocumentsByUserId_ShouldReturnJsonArray() throws Exception {
        UUID userId = UUID.randomUUID();
        Document doc = new Document();
        doc.setName("Sample Doc");

        when(documentService.findAllByCreatedBy(userId)).thenReturn(List.of(doc));

        mockMvc.perform(get("/documents/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sample Doc"));
    }

    @Test
    void countDocuments_ShouldReturnNumber() throws Exception {
        when(documentService.countAllDocuments()).thenReturn(5L);

        mockMvc.perform(get("/documents/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }
}
