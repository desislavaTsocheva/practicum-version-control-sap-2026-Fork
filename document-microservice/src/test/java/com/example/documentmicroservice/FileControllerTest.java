package com.example.documentmicroservice;

import com.example.documentmicroservice.controllers.FileController;
import com.example.documentmicroservice.models.Document;
import com.example.documentmicroservice.models.Version;
import com.example.documentmicroservice.services.DocumentService;
import com.example.documentmicroservice.services.FileService;
import com.example.documentmicroservice.services.VersionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebMvcTest(FileController.class)
class FileControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public DocumentService documentService;

    @MockBean
    public VersionService versionService;

    @MockBean
    public FileService fileService;

    @Test
    void handleUpload_ShouldSaveAndRedirect() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();

        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", "content".getBytes());

        Document mockDoc = new Document();
        Version mockVersion = new Version();
        mockVersion.setId(UUID.randomUUID());

        when(documentService.saveDocument(any(), eq(projectId), any(), eq(userId))).thenReturn(mockDoc);
        when(versionService.saveVersion(eq(userId), eq(mockDoc))).thenReturn(mockVersion);

        mockMvc.perform(multipart("/upload")
                        .file(mockFile)
                        .param("userId", userId.toString())
                        .param("projectId", projectId.toString())
                        .param("username", "testUser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:8080/document-microservice/documents?userId=" + userId + "&name=testUser"));

        verify(fileService, times(1)).saveFile(any(), any());
    }

    @Test
    void handleUpload_ShouldRedirectWithError_WhenFileIsTooLarge() throws Exception {
        UUID userId = UUID.randomUUID();
        byte[] largeContent = new byte[11 * 1024 * 1024]; // 11MB
        MockMultipartFile largeFile = new MockMultipartFile(
                "file", "big.pdf", "application/pdf", largeContent);

        mockMvc.perform(multipart("/upload")
                        .file(largeFile)
                        .param("userId", userId.toString())
                        .param("projectId", UUID.randomUUID().toString())
                        .param("username", "user"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:8080/document-microservice/documents?userId=" + userId + "&name=user&errorType=largeFile"));
    }

    @Test
    void uploadVersions_ShouldSaveNewVersionAndRedirect() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID docId = UUID.randomUUID();
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "v2.txt", "text/plain", "new content".getBytes());

        Document mockDoc = new Document();
        Version mockVersion = new Version();
        mockVersion.setId(UUID.randomUUID());
        mockVersion.setVersionNumber(2);

        when(documentService.findById(docId)).thenReturn(mockDoc);
        when(versionService.saveVersion(eq(userId), eq(mockDoc))).thenReturn(mockVersion);

        mockMvc.perform(multipart("/uploadVersion")
                        .file(mockFile)
                        .param("userId", userId.toString())
                        .param("docId", docId.toString())
                        .param("name", "user"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:8080/document-microservice/documents?userId=" + userId + "&name=user"));

        verify(fileService, times(1)).saveFile(any(), any());
    }
}
