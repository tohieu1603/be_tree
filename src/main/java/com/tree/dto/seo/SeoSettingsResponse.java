package com.tree.dto.seo;

import com.tree.entity.SeoSettings;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeoSettingsResponse {
    private String id;
    private String siteName;
    private String siteUrl;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;

    // Robots
    private boolean robotsAllowAll;
    private String robotsDisallowPaths;
    private String robotsCustomRules;

    // Sitemap
    private boolean sitemapEnabled;
    private boolean sitemapIncludeArticles;
    private boolean sitemapIncludeCategories;
    private String sitemapChangeFrequency;

    // Analytics & Verification
    private String googleAnalyticsId;
    private String googleVerification;

    // Social
    private String ogImage;
    private String twitterHandle;

    private String createdAt;
    private String updatedAt;

    public static SeoSettingsResponse from(SeoSettings settings) {
        if (settings == null) {
            return getDefault();
        }
        return SeoSettingsResponse.builder()
                .id(settings.getId().toString())
                .siteName(settings.getSiteName())
                .siteUrl(settings.getSiteUrl())
                .metaTitle(settings.getMetaTitle())
                .metaDescription(settings.getMetaDescription())
                .metaKeywords(settings.getMetaKeywords())
                .robotsAllowAll(settings.isRobotsAllowAll())
                .robotsDisallowPaths(settings.getRobotsDisallowPaths())
                .robotsCustomRules(settings.getRobotsCustomRules())
                .sitemapEnabled(settings.isSitemapEnabled())
                .sitemapIncludeArticles(settings.isSitemapIncludeArticles())
                .sitemapIncludeCategories(settings.isSitemapIncludeCategories())
                .sitemapChangeFrequency(settings.getSitemapChangeFrequency())
                .googleAnalyticsId(settings.getGoogleAnalyticsId())
                .googleVerification(settings.getGoogleVerification())
                .ogImage(settings.getOgImage())
                .twitterHandle(settings.getTwitterHandle())
                .createdAt(settings.getCreatedAt().toString())
                .updatedAt(settings.getUpdatedAt().toString())
                .build();
    }

    public static SeoSettingsResponse getDefault() {
        return SeoSettingsResponse.builder()
                .siteName("Tree")
                .siteUrl("http://localhost:3000")
                .metaTitle("Tree - Product Website")
                .metaDescription("Product introduction and blog website")
                .robotsAllowAll(true)
                .robotsDisallowPaths("/admin/,/api/")
                .sitemapEnabled(true)
                .sitemapIncludeArticles(true)
                .sitemapIncludeCategories(true)
                .sitemapChangeFrequency("weekly")
                .build();
    }
}
