package com.tree.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteSettingsDTO {
    // Basic Info
    private String siteName;
    private String siteTagline;
    private String siteDescription;

    // Logo & Branding
    private String logoUrl;
    private String logoDarkUrl;
    private String faviconUrl;

    // Contact Info
    private String contactEmail;
    private String contactPhone;
    private String contactAddress;

    // Social Links
    private String facebookUrl;
    private String instagramUrl;
    private String youtubeUrl;
    private String tiktokUrl;
    private String zaloUrl;

    // Footer Content
    private String footerText;
    private String copyrightText;

    // Homepage Content
    private String heroTitle;
    private String heroSubtitle;
    private String categorySectionTitle;
    private String categorySectionSubtitle;
    private String serviceSectionTitle;

    // Services
    private List<ServiceItem> services;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceItem {
        private String title;
        private String description;
        private String imageUrl;
        private String linkText;
        private String linkUrl;
    }
}
