package com.example.documentmicroservice.services;

import com.example.documentmicroservice.models.File;
import com.example.documentmicroservice.repositories.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FileService {
    private final FileRepository fileRepository;
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
    public List<File> getAllFiles() {return fileRepository.findAll();}
    @Transactional
    public File saveFile(File file) {
        return fileRepository.save(file);
    }
}
