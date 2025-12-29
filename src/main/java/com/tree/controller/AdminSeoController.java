package com.tree.controller;

import com.tree.dto.ApiResponse;
import com.tree.dto.seo.SeoSettingsRequest;
import com.tree.dto.seo.SeoSettingsResponse;
import com.tree.service.SeoSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/seo")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin - SEO", description = "SEO settings management")
public class AdminSeoController {

    private final SeoSettingsService seoSettingsService;

    @GetMapping
    @Operation(summary = "Get SEO settings")
    public ResponseEntity<ApiResponse<SeoSettingsResponse>> getSettings() {
        return ResponseEntity.ok(ApiResponse.success(seoSettingsService.getSettings()));
    }

    @PutMapping
    @Operation(summary = "Update SEO settings")
    public ResponseEntity<ApiResponse<SeoSettingsResponse>> updateSettings(@RequestBody SeoSettingsRequest request) {
        return ResponseEntity.ok(ApiResponse.success(seoSettingsService.saveSettings(request)));
    }
}
