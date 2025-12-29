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

    private String featuredImage;
    private String featuredImageAlt;

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

    // SEO
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private String canonicalUrl;

    private String status;
    private String categoryId;
}
