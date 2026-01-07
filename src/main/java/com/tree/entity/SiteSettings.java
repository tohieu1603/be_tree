package com.tree.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "site_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SiteSettings extends BaseEntity {

    // Basic Info
    @Column(name = "site_name", nullable = false)
    private String siteName = "Tree";

    @Column(name = "site_url")
    private String siteUrl;

    @Column(name = "site_tagline")
    private String siteTagline;

    @Column(name = "site_description", columnDefinition = "TEXT")
    private String siteDescription;

    // Logo & Branding
    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "logo_dark_url")
    private String logoDarkUrl;

    @Column(name = "favicon_url")
    private String faviconUrl;

    // Contact Info
    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "contact_address", columnDefinition = "TEXT")
    private String contactAddress;

    // Social Links
    @Column(name = "facebook_url")
    private String facebookUrl;

    @Column(name = "instagram_url")
    private String instagramUrl;

    @Column(name = "youtube_url")
    private String youtubeUrl;

    @Column(name = "tiktok_url")
    private String tiktokUrl;

    @Column(name = "zalo_url")
    private String zaloUrl;

    // Footer Content
    @Column(name = "footer_text", columnDefinition = "TEXT")
    private String footerText;

    @Column(name = "copyright_text")
    private String copyrightText;

    // Homepage Content
    @Column(name = "hero_title")
    private String heroTitle;

    @Column(name = "hero_subtitle")
    private String heroSubtitle;

    @Column(name = "category_section_title")
    private String categorySectionTitle;

    @Column(name = "category_section_subtitle")
    private String categorySectionSubtitle;

    @Column(name = "service_section_title")
    private String serviceSectionTitle;

    // Services (JSON array stored as text)
    @Column(name = "services_json", columnDefinition = "TEXT")
    private String servicesJson;

    // Navigation Menu (JSON arrays for menu items)
    // Each item: { label, href }
    @Column(name = "nav_left_menu_json", columnDefinition = "TEXT")
    private String navLeftMenuJson;

    @Column(name = "nav_right_menu_json", columnDefinition = "TEXT")
    private String navRightMenuJson;

    // Theme Settings
    // Font preset: elegant, modern, minimal, luxury, classic, artistic
    @Column(name = "font_preset")
    private String fontPreset = "elegant";

    // Color palette: warmGold, deepBrown, honeyAmber, antiqueGold, rosewood, sandalwood
    @Column(name = "color_palette")
    private String colorPalette = "warmGold";

    // Border radius preset: none, subtle, rounded, pill
    @Column(name = "border_radius")
    private String borderRadius = "subtle";
}
