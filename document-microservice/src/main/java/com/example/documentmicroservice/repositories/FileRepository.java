package com.example.documentmicroservice.repositories;

import com.example.documentmicroservice.models.Document;
import com.example.documentmicroservice.models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {
    List<File> findByVersionId(UUID versionId);
}
