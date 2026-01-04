package com.tree.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "content_html", columnDefinition = "TEXT")
    private String contentHtml;

    // Featured Image (ảnh đại diện)
    @Column(name = "featured_image")
    private String featuredImage;

    @Column(name = "featured_image_alt")
    private String featuredImageAlt;

    @Column(name = "featured_image_caption")
    private String featuredImageCaption;

    // Gallery Images - JSON array: [{"url":"...", "alt":"...", "caption":"..."}]
    @Column(name = "gallery_images", columnDefinition = "TEXT")
    private String galleryImages;

    // Open Graph Image (for social sharing, if different from featured)
    @Column(name = "og_image")
    private String ogImage;

    @Column(name = "og_image_alt")
    private String ogImageAlt;

    // Tags stored as comma-separated
    @Column(columnDefinition = "TEXT")
    private String tags;

    // Reading time in minutes
    @Column(name = "reading_time")
    private Integer readingTime;

    // Featured article flag
    @Builder.Default
    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    // Allow comments
    @Builder.Default
    @Column(name = "allow_comments")
    private Boolean allowComments = true;

    // Scheduled publish date
    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    // Source/reference link
    @Column(name = "source_url")
    private String sourceUrl;

    // ==================== SEO Fields (Yoast/RankMath style) ====================

    // Focus Keyword - từ khóa chính để tối ưu
    @Column(name = "focus_keyword")
    private String focusKeyword;

    // Meta Title (50-60 ký tự)
    @Column(name = "meta_title")
    private String metaTitle;

    // Meta Description (150-160 ký tự)
    @Column(name = "meta_description", length = 320)
    private String metaDescription;

    // Meta Keywords (comma-separated)
    @Column(name = "meta_keywords")
    private String metaKeywords;

    // Canonical URL
    @Column(name = "canonical_url")
    private String canonicalUrl;

    // Robots meta: index/noindex, follow/nofollow
    @Builder.Default
    @Column(name = "robots_index")
    private Boolean robotsIndex = true;

    @Builder.Default
    @Column(name = "robots_follow")
    private Boolean robotsFollow = true;

    // Open Graph
    @Column(name = "og_title")
    private String ogTitle;

    @Column(name = "og_description")
    private String ogDescription;

    // Twitter Card
    @Column(name = "twitter_title")
    private String twitterTitle;

    @Column(name = "twitter_description")
    private String twitterDescription;

    @Column(name = "twitter_image")
    private String twitterImage;

    // Schema.org / Structured Data
    @Column(name = "schema_type")
    private String schemaType; // Article, NewsArticle, BlogPosting

    // SEO Score (calculated, 0-100)
    @Column(name = "seo_score")
    private Integer seoScore;

    // Readability Score (0-100)
    @Column(name = "readability_score")
    private Integer readabilityScore;

    // ==================== End SEO Fields ====================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.DRAFT;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    public enum Status {
        DRAFT, PUBLISHED, ARCHIVED
    }
}
