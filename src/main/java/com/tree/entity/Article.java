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

    @Column(name = "featured_image")
    private String featuredImage;

    @Column(name = "featured_image_alt")
    private String featuredImageAlt;

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

    // SEO fields
    @Column(name = "meta_title")
    private String metaTitle;

    @Column(name = "meta_description")
    private String metaDescription;

    @Column(name = "meta_keywords")
    private String metaKeywords;

    @Column(name = "canonical_url")
    private String canonicalUrl;

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
