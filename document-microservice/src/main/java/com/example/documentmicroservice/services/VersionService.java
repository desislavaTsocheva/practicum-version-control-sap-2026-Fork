package com.example.documentmicroservice.services;
import com.example.documentmicroservice.models.Document;
import com.example.documentmicroservice.models.Version;
import com.example.documentmicroservice.repositories.VersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class VersionService {

    private final VersionRepository versionRepository;

    public VersionService(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    public List<Version> getAllVersions() {
        return (List<Version>) versionRepository.findAll();
    }

    @Transactional
    public Version saveVersion(UUID userId, Document document) {
        Version version = new Version();
        version.setVersionNumber(1);
        version.setMessage(document.getName());
        version.setDocumentId(document.getId());
        version.setUserId(userId);
        version.setActive(true);
        version.setApproved(false);
        return versionRepository.save(version);
    }

    public List<Version> findAllByCreatedBy(UUID userId) {
        return versionRepository.findByUserId(userId);
    }
}