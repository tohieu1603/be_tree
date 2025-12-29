package com.tree.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seo_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeoSettings extends BaseEntity {

    @Column(name = "site_name")
    private String siteName;

    @Column(name = "site_url")
    private String siteUrl;

    @Column(name = "meta_title")
    private String metaTitle;

    @Column(name = "meta_description", length = 500)
    private String metaDescription;

    @Column(name = "meta_keywords", length = 500)
    private String metaKeywords;

    // Robots.txt settings
    @Column(name = "robots_allow_all")
    @Builder.Default
    private boolean robotsAllowAll = true;

    @Column(name = "robots_disallow_paths", length = 1000)
    @Builder.Default
    private String robotsDisallowPaths = "/admin/,/api/";

    @Column(name = "robots_custom_rules", length = 2000)
    private String robotsCustomRules;

    // Sitemap settings
    @Column(name = "sitemap_enabled")
    @Builder.Default
    private boolean sitemapEnabled = true;

    @Column(name = "sitemap_include_articles")
    @Builder.Default
    private boolean sitemapIncludeArticles = true;

    @Column(name = "sitemap_include_categories")
    @Builder.Default
    private boolean sitemapIncludeCategories = true;

    @Column(name = "sitemap_change_frequency")
    @Builder.Default
    private String sitemapChangeFrequency = "weekly";

    // Google Analytics
    @Column(name = "google_analytics_id")
    private String googleAnalyticsId;

    // Google Search Console
    @Column(name = "google_verification")
    private String googleVerification;

    // Social media
    @Column(name = "og_image")
    private String ogImage;

    @Column(name = "twitter_handle")
    private String twitterHandle;
}
