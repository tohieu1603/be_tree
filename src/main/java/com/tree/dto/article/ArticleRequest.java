package com.tree.dto.article;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String slug;
    private String summary;

    @NotBlank(message = "Content is required")
    private String content;

    // Content blocks as JSON - for block editor
    private String contentBlocks;

    // Table of contents as JSON - auto-generated from headings
    private String tableOfContents;

    // Featured Image
    private String featuredImage;
    private String featuredImageAlt;
    private Integer featuredImageWidth;
    private Integer featuredImageHeight;
    private String featuredImageCaption;

    // Gallery Images - JSON array: [{"url":"...", "alt":"...", "caption":"..."}]
    private String galleryImages;

    // Open Graph Image (for social sharing)
    private String ogImage;
    private String ogImageAlt;

    // Tags as comma-separated string
    private String tags;

    // Reading time in minutes
    private Integer readingTime;

    // Featured article
    private Boolean isFeatured;

    // Allow comments
    private Boolean allowComments;

    // Scheduled publish date
    private LocalDateTime publishedAt;

    // Source/reference
    private String sourceUrl;

    // ==================== SEO Fields ====================
    private String focusKeyword;      // Từ khóa chính
    private String metaTitle;         // 50-60 ký tự
    private String metaDescription;   // 150-160 ký tự
    private String metaKeywords;      // Comma-separated
    private String canonicalUrl;

    // Robots
    private Boolean robotsIndex;      // default true
    private Boolean robotsFollow;     // default true

    // Open Graph
    private String ogTitle;
    private String ogDescription;

    // Twitter Card
    private String twitterTitle;
    private String twitterDescription;
    private String twitterImage;

    // Schema.org
    private String schemaType;        // Article, NewsArticle, BlogPosting

    private String status;
    private String categoryId;
}
