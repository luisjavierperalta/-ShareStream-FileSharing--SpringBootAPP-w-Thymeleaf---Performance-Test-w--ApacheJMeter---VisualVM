package com.practice.sharestream.controller;

import com.practice.sharestream.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/upload")
    public String uploadForm() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        String filename = fileService.save(file);
        model.addAttribute("message", "File uploaded successfully: " + filename);
        return "upload";
    }

    @GetMapping("/files")
    public String listUploadedFiles(Model model) {
        try {
            // Load the file directory
            Path fileStorageLocation = fileService.load("");

            // List all files in the directory
            List<String> fileNames = Files.list(fileStorageLocation)
                    .filter(Files::isRegularFile) // Ensure it's a regular file
                    .map(Path::getFileName) // Get the file name
                    .map(Path::toString) // Convert to String
                    .collect(Collectors.toList());

            // Add file names to the model
            model.addAttribute("files", fileNames);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception
            model.addAttribute("message", "Could not list files");
        }

        return "download";
    }


    @GetMapping("/files/{filename}")
    @ResponseBody
    public Resource serveFile(@PathVariable String filename) {
        Path file = fileService.load(filename);
        Resource resource;
        try {
            resource = new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file", e);
        }
        return resource;
    }
}

