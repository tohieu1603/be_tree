package com.tree.controller;

import com.tree.dto.ApiResponse;
import com.tree.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/upload")
@RequiredArgsConstructor
@Tag(name = "Admin - File Upload", description = "File upload management")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @PostMapping
    @Operation(summary = "Upload single file")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "articles") String folder) {

        String filePath = fileStorageService.storeFile(file, folder);

        Map<String, String> result = new HashMap<>();
        result.put("url", filePath);
        result.put("filename", filePath.substring(filePath.lastIndexOf("/") + 1));

        return ResponseEntity.ok(ApiResponse.success("File uploaded successfully", result));
    }

    @PostMapping("/multiple")
    @Operation(summary = "Upload multiple files")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> uploadMultipleFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "folder", defaultValue = "articles") String folder) {

        List<Map<String, String>> results = new ArrayList<>();

        for (MultipartFile file : files) {
            String filePath = fileStorageService.storeFile(file, folder);

            Map<String, String> result = new HashMap<>();
            result.put("url", filePath);
            result.put("filename", filePath.substring(filePath.lastIndexOf("/") + 1));
            results.add(result);
        }

        return ResponseEntity.ok(ApiResponse.success("Files uploaded successfully", results));
    }

    @PostMapping("/category")
    @Operation(summary = "Upload category image")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadCategoryImage(
            @RequestParam("file") MultipartFile file) {
        return uploadFile(file, "categories");
    }

    @PostMapping("/product")
    @Operation(summary = "Upload product image")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadProductImage(
            @RequestParam("file") MultipartFile file) {
        return uploadFile(file, "products");
    }

    @PostMapping("/banner")
    @Operation(summary = "Upload banner image")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadBannerImage(
            @RequestParam("file") MultipartFile file) {
        return uploadFile(file, "banners");
    }

    @PostMapping("/logo")
    @Operation(summary = "Upload logo image")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadLogoImage(
            @RequestParam("file") MultipartFile file) {
        return uploadFile(file, "logos");
    }

    @PostMapping("/article")
    @Operation(summary = "Upload article image")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadArticleImage(
            @RequestParam("file") MultipartFile file) {
        return uploadFile(file, "articles");
    }

    @DeleteMapping
    @Operation(summary = "Delete file")
    public ResponseEntity<ApiResponse<Void>> deleteFile(@RequestParam("path") String filePath) {
        boolean deleted = fileStorageService.deleteFile(filePath);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success("File deleted successfully", null));
        }
        return ResponseEntity.ok(ApiResponse.success("File not found or already deleted", null));
    }
}
