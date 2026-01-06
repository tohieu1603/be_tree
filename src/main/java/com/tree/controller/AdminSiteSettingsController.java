package com.tree.controller;

import com.tree.dto.ApiResponse;
import com.tree.dto.SiteSettingsDTO;
import com.tree.service.SiteSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/site-settings")
@RequiredArgsConstructor
@Tag(name = "Admin - Site Settings", description = "Site configuration management")
public class AdminSiteSettingsController {

    private final SiteSettingsService siteSettingsService;

    @GetMapping
    @Operation(summary = "Get site settings")
    public ResponseEntity<ApiResponse<SiteSettingsDTO>> getSettings() {
        return ResponseEntity.ok(ApiResponse.success(siteSettingsService.getSettings()));
    }

    @PutMapping
    @Operation(summary = "Update site settings")
    public ResponseEntity<ApiResponse<SiteSettingsDTO>> updateSettings(@RequestBody SiteSettingsDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Settings saved", siteSettingsService.saveSettings(dto)));
    }
}
