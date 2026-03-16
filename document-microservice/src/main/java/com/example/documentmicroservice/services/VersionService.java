package com.example.documentmicroservice.services;

import com.example.documentmicroservice.models.Version;
import com.example.documentmicroservice.repositories.VersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
    public Version saveVersion(Version version) {
        return versionRepository.save(version);
    }
}