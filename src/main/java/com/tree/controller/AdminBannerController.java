package com.tree.controller;

import com.tree.dto.ApiResponse;
import com.tree.dto.BannerDTO;
import com.tree.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/banners")
@RequiredArgsConstructor
@Tag(name = "Admin - Banners", description = "Banner slideshow management")
public class AdminBannerController {

    private final BannerService bannerService;

    @GetMapping
    @Operation(summary = "Get all banners")
    public ResponseEntity<ApiResponse<List<BannerDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getAllBanners()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get banner by ID")
    public ResponseEntity<ApiResponse<BannerDTO>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getBannerById(id)));
    }

    @PostMapping
    @Operation(summary = "Create banner")
    public ResponseEntity<ApiResponse<BannerDTO>> create(@RequestBody BannerDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Banner created", bannerService.createBanner(dto)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update banner")
    public ResponseEntity<ApiResponse<BannerDTO>> update(@PathVariable UUID id, @RequestBody BannerDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Banner updated", bannerService.updateBanner(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete banner")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.ok(ApiResponse.success("Banner deleted", null));
    }
}
