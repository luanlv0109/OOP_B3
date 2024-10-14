package com.example.da.Service;

import com.example.da.Service.impl.FilesStorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FilesStorageServiceTest {


    private FilesStorageServiceImpl filesStorageService;
    private Path root;

    @BeforeEach
    void setUp() throws IOException {
        filesStorageService = new FilesStorageServiceImpl();
        root = Paths.get("./uploads");
        filesStorageService.init(); // Khởi tạo thư mục uploads

        // Tạo file testfile.txt trong thư mục tạm thời
        Path testFile = Paths.get("./src/test/resources/testfile.txt");
        Files.createDirectories(testFile.getParent()); // Tạo thư mục nếu nó không tồn tại
        Files.writeString(testFile, "This is a test file."); // Ghi nội dung vào file
    }


    @Test
    void init_ShouldCreateUploadDirectory() throws IOException {
        // Act
        if (Files.exists(root)) {
            Files.walk(root)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(file -> {
                        if (file.exists()) { // Kiểm tra sự tồn tại của file trước khi xóa
                            file.delete();
                        }
                    });
            Files.deleteIfExists(root); // Xóa thư mục nếu nó đã tồn tại
        }

        filesStorageService.init();

        // Assert
        assertTrue(Files.exists(root), "Upload directory should be created");
    }


    @Test
    void save_ShouldSaveFileAndReturnFilename() throws IOException {
        // Arrange
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        String originalFilename = "testfile.txt";
        when(mockFile.getOriginalFilename()).thenReturn(originalFilename);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("This is a test file.".getBytes())); // Giả lập luồng dữ liệu cho file

        // Act
        String savedFileName = filesStorageService.save(mockFile);

        // Assert
        assertNotNull(savedFileName, "Saved filename should not be null");
        assertTrue(savedFileName.contains(originalFilename), "Saved filename should contain the original filename"); // Sửa dòng này
        assertTrue(Files.exists(root.resolve(savedFileName)), "File should be saved in upload directory");
    }




    @Test
    void load_ShouldReturnResource_WhenFileExists() throws Exception {
        // Arrange
        String filename = "testfile.txt";
        Path testFilePath = root.resolve(filename);

        // Xóa tệp nếu nó đã tồn tại trước đó
        if (Files.exists(testFilePath)) {
            Files.delete(testFilePath);
        }

        Files.createFile(testFilePath); // Tạo file test để kiểm tra

        // Act
        Resource resource = filesStorageService.load(filename);

        // Assert
        assertNotNull(resource);
        assertTrue(resource.exists(), "Resource should exist");
        assertEquals(testFilePath.toUri(), resource.getURI(), "Resource URI should match the file URI");
    }

    @Test
    void load_ShouldThrowException_WhenFileDoesNotExist() {
        // Arrange
        String filename = "nonexistent.txt";

        // Act & Assert
        assertThrows(MalformedURLException.class, () -> {
            filesStorageService.load(filename);
        });
    }
}
