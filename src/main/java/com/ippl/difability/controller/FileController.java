
package com.ippl.difability.controller;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ippl.difability.service.FileStorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/files/")
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;

    @PostMapping("/upload/image")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        return fileStorageService.save(file, "images");
    }

    @PostMapping("/upload/document")
    public String uploadDocument(@RequestParam("file") MultipartFile file) throws IOException {
        return fileStorageService.save(file, "documents");
    }

    @GetMapping("/view")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> view(@RequestParam String path) throws IOException {
        Resource file = fileStorageService.load(path);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
            .body(file);
    }
}
