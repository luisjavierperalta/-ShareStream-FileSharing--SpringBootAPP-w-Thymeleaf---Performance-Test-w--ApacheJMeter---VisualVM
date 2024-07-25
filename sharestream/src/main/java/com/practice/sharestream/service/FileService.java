package com.practice.sharestream.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public String save(MultipartFile file) {
        try {
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path path = Paths.get(uploadDir, filename);
            Files.copy(file.getInputStream(), path);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }

    public Path load(String filename) {
        return Paths.get(uploadDir).resolve(filename);
    }
}

