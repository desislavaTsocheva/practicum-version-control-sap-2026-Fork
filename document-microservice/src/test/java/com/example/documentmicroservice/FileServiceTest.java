package com.example.documentmicroservice;

import com.example.documentmicroservice.models.File;
import com.example.documentmicroservice.models.Version;
import com.example.documentmicroservice.repositories.FileRepository;
import com.example.documentmicroservice.repositories.VersionRepository;
import com.example.documentmicroservice.services.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private VersionRepository versionRepository;

    @InjectMocks
    private FileService fileService;

    @Test
    void saveFile_ShouldReturnSavedFileWithCorrectMetadata() throws IOException {
        UUID versionId = UUID.randomUUID();
        MultipartFile mockMultipart = mock(MultipartFile.class);
        byte[] content = "test content".getBytes();

        when(mockMultipart.getBytes()).thenReturn(content);
        when(mockMultipart.getContentType()).thenReturn("application/pdf");
        when(fileRepository.save(any(File.class))).thenAnswer(i -> i.getArgument(0));

        File result = fileService.saveFile(versionId, mockMultipart);

        assertNotNull(result);
        assertEquals(versionId, result.getVersionId());
        assertArrayEquals(content, result.getFile_path());
        assertEquals("application/pdf", result.getContent());
        assertNotNull(result.getHistoryTimestamp());
    }

    @Test
    void getFileEntityByVersionId_ShouldReturnFile_WhenVersionAndFileExist() {
        UUID versionId = UUID.randomUUID();
        Version mockVersion = new Version();
        mockVersion.setId(versionId);

        File mockFile = new File();
        mockFile.setFile_path("data".getBytes());

        when(versionRepository.findById(versionId)).thenReturn(Optional.of(mockVersion));
        when(fileRepository.findByVersionId(versionId)).thenReturn(Optional.of(mockFile));

        File result = fileService.getFileEntityByVersionId(versionId);

        assertNotNull(result);
        assertArrayEquals("data".getBytes(), result.getFile_path());
    }

    @Test
    void getFileEntityByVersionId_ShouldThrowException_WhenVersionNotFound() {
        UUID versionId = UUID.randomUUID();
        when(versionRepository.findById(versionId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> fileService.getFileEntityByVersionId(versionId));
    }

    @Test
    void saveFile_ShouldThrowRuntimeException_OnIOException() throws IOException {
        UUID versionId = UUID.randomUUID();
        MultipartFile mockMultipart = mock(MultipartFile.class);

        when(mockMultipart.getBytes()).thenThrow(new IOException("Read error"));

        assertThrows(RuntimeException.class, () -> fileService.saveFile(versionId, mockMultipart));
    }
}