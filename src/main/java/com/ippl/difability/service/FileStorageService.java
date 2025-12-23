package com.ippl.difability.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ippl.difability.exception.InvalidFileException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FileStorageService {
    private final Path root = Paths.get("uploads");

    public String save(MultipartFile file, String folder) throws IOException{
        validate(file, folder);
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path dir = root.resolve(folder);
        Files.createDirectories(dir);

        Path target = dir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "uploads/" + folder + "/" + filename;
    }

    public Resource load(String relativePath) throws IOException{
        Path file = Paths.get(relativePath);
        return new UrlResource(file.toUri());
    }

    private void validate(MultipartFile file, String folder) {
        if (file == null || file.isEmpty())
            throw new InvalidFileException("File is empty.");

        String type = file.getContentType();
        if (type == null)
            throw new InvalidFileException("Unknown file type.");
        String name = file.getOriginalFilename().toLowerCase();

        switch (folder) {

            case "images" ->{
                if(!(type.equals("image/png") || type.equals("image/jpeg") || type.equals("image/jpg"))){
                    throw new InvalidFileException("Images must be PNG, JPG, or JPEG.");
                }
                if (!(name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"))){
                    throw new InvalidFileException("Invalid image extension.");
                }
            }

            case "documents" ->{
                if (!type.equals("application/pdf")){
                    throw new InvalidFileException("Documents must be PDF.");
                }
                if (!name.endsWith(".pdf")){
                    throw new InvalidFileException("Invalid PDF extension.")  ;
                }
            }
            default 
                -> throw new InvalidFileException("Unknown upload folder.");
        }
    }
}
