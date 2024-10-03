package com.example.da.Service.impl;

import com.example.da.Service.FilesStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class FilesStorageServiceImpl implements FilesStorageService {
    private final   Path root = Paths.get("./uploads");


    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String save(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString() + "_" + originalFilename;  // Tạo tên file duy nhất
        Path destinationFile = this.root.resolve(filename).normalize().toAbsolutePath();

        // Lưu file mới với tên độc nhất
        Files.copy(file.getInputStream(), destinationFile);

        return destinationFile.getFileName().toString();  // Trả về tên file đã lưu
    }


    @Override
    public Resource load(String filename) throws MalformedURLException {
        Path file = root.resolve(filename);
        Resource resource = (Resource) new UrlResource(file.toUri());
        return resource;

    }
}
