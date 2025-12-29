package com.tree.dto.seo;

import lombok.Data;

@Data
public class SeoSettingsRequest {
    private String siteName;
    private String siteUrl;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;

    // Robots
    private boolean robotsAllowAll = true;
    private String robotsDisallowPaths;
    private String robotsCustomRules;

    // Sitemap
    private boolean sitemapEnabled = true;
    private boolean sitemapIncludeArticles = true;
    private boolean sitemapIncludeCategories = true;
    private String sitemapChangeFrequency = "weekly";

    // Analytics & Verification
    private String googleAnalyticsId;
    private String googleVerification;

    // Social
    private String ogImage;
    private String twitterHandle;
}
