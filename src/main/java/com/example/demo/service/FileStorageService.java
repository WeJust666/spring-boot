package com.example.demo.service;

import com.example.demo.config.FileStorageProperties;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;
    public FileStorageService(FileStorageProperties properties) throws IOException {
        this.fileStorageLocation= Paths.get(properties.getUploadDir()).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    public String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public Path getFilePath(String fileName) {
        return this.fileStorageLocation.resolve(fileName).normalize();
    }
}
