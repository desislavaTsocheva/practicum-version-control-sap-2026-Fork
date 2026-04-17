package com.example.documentmicroservice;

import com.example.documentmicroservice.models.Document;
import com.example.documentmicroservice.models.Version;
import com.example.documentmicroservice.repositories.FileRepository;
import com.example.documentmicroservice.repositories.VersionRepository;
import com.example.documentmicroservice.services.VersionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VersionServiceTest {

    @Mock
    private VersionRepository versionRepository;

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private VersionService versionService;

    @Test
    void saveVersion_ShouldSetVersionOneAsActiveAndApproved() {
        UUID userId = UUID.randomUUID();
        Document doc = new Document();
        doc.setId(UUID.randomUUID());
        doc.setName("test.pdf");

        when(versionRepository.findMaxVersionNumberByDocumentId(doc.getId())).thenReturn(null);
        when(versionRepository.save(any(Version.class))).thenAnswer(i -> i.getArgument(0));

        Version result = versionService.saveVersion(userId, doc);

        assertEquals(1, result.getVersionNumber());
        assertTrue(result.isActive());
        assertTrue(result.isApproved());
        assertEquals(doc.getId(), result.getDocumentId());
    }

    @Test
    void saveVersion_ShouldIncrementVersionAndSetInActive() {
        UUID userId = UUID.randomUUID();
        Document doc = new Document();
        doc.setId(UUID.randomUUID());

        when(versionRepository.findMaxVersionNumberByDocumentId(doc.getId())).thenReturn(1);
        when(versionRepository.save(any(Version.class))).thenAnswer(i -> i.getArgument(0));

        Version result = versionService.saveVersion(userId, doc);

        assertEquals(2, result.getVersionNumber());
        assertFalse(result.isActive());
        assertFalse(result.isApproved());
    }

    @Test
    void approveVersion_ShouldSetApprovedAndActive() {
        UUID versionId = UUID.randomUUID();
        Version version = new Version();
        version.setApproved(false);
        version.setActive(false);

        when(versionRepository.findById(versionId)).thenReturn(Optional.of(version));
        when(versionRepository.save(any(Version.class))).thenAnswer(i -> i.getArgument(0));

        versionService.approveVersion(versionId);

        assertTrue(version.isApproved());
        assertTrue(version.isActive());
        verify(versionRepository).save(version);
    }

    @Test
    void deleteVersion_ShouldCallBothRepositories() {
        UUID versionId = UUID.randomUUID();

        versionService.deleteVersion(versionId);

        verify(fileRepository, times(1)).deleteByVersionId(versionId);
        verify(versionRepository, times(1)).deleteById(versionId);
    }

    @Test
    void findAllByDocumentIds_ShouldReturnEmptyList_WhenIdsAreNull() {
        List<Version> result = versionService.findAllByDocumentIds(null);
        assertTrue(result.isEmpty());
    }
}