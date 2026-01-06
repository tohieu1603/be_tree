package com.tree.service;

import com.tree.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.upload.max-size:10485760}")
    private long maxFileSize; // 10MB default

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg", "image/png", "image/gif", "image/webp", "image/svg+xml"
    );

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
        "jpg", "jpeg", "png", "gif", "webp", "svg"
    );

    @PostConstruct
    public void init() {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("Created upload directory: {}", uploadPath.toAbsolutePath());
            }

            // Create subdirectories
            for (String subDir : Arrays.asList("categories", "products", "articles", "banners", "logos")) {
                Path subPath = uploadPath.resolve(subDir);
                if (!Files.exists(subPath)) {
                    Files.createDirectories(subPath);
                    log.info("Created subdirectory: {}", subPath.toAbsolutePath());
                }
            }
        } catch (IOException e) {
            log.error("Could not create upload directory", e);
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    /**
     * Store file with auto-generated unique name
     */
    public String storeFile(MultipartFile file, String folder) {
        validateFile(file);

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = getExtension(originalFilename);
        String newFilename = UUID.randomUUID().toString() + "." + extension;

        return storeFileWithName(file, folder, newFilename);
    }

    /**
     * Store file with custom name
     */
    public String storeFileWithName(MultipartFile file, String folder, String filename) {
        validateFile(file);

        try {
            Path folderPath = Paths.get(uploadDir, folder);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
                log.info("Created folder: {}", folderPath.toAbsolutePath());
            }

            Path targetPath = folderPath.resolve(filename);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            String relativePath = "/uploads/" + folder + "/" + filename;
            log.info("File stored: {}", relativePath);
            return relativePath;

        } catch (IOException e) {
            log.error("Could not store file {}", filename, e);
            throw new BadRequestException("Could not store file: " + e.getMessage());
        }
    }

    /**
     * Delete file
     */
    public boolean deleteFile(String filePath) {
        try {
            // Remove leading /uploads/ if present
            String relativePath = filePath.startsWith("/uploads/")
                ? filePath.substring(9)
                : filePath;

            Path path = Paths.get(uploadDir, relativePath);
            if (Files.exists(path)) {
                Files.delete(path);
                log.info("File deleted: {}", filePath);
                return true;
            }
            return false;
        } catch (IOException e) {
            log.error("Could not delete file {}", filePath, e);
            return false;
        }
    }

    /**
     * Check if file exists
     */
    public boolean fileExists(String filePath) {
        String relativePath = filePath.startsWith("/uploads/")
            ? filePath.substring(9)
            : filePath;
        Path path = Paths.get(uploadDir, relativePath);
        return Files.exists(path);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        if (file.getSize() > maxFileSize) {
            throw new BadRequestException("File size exceeds maximum allowed size of " + (maxFileSize / 1024 / 1024) + "MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new BadRequestException("File type not allowed. Allowed types: " + String.join(", ", ALLOWED_IMAGE_TYPES));
        }

        String filename = file.getOriginalFilename();
        if (filename != null) {
            String extension = getExtension(filename).toLowerCase();
            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                throw new BadRequestException("File extension not allowed. Allowed: " + String.join(", ", ALLOWED_EXTENSIONS));
            }
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "jpg";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
