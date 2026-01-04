package com.tree.dto.article;

import com.tree.dto.category.CategoryResponse;
import com.tree.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {
    private String id;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String contentHtml;
    private String featuredImage;
    private String featuredImageAlt;
    private String featuredImageCaption;
    private String galleryImages;
    private String ogImage;
    private String ogImageAlt;
    private String tags;
    private Integer readingTime;
    private Boolean isFeatured;
    private Boolean allowComments;
    private LocalDateTime publishedAt;
    private String sourceUrl;
    // SEO Fields
    private String focusKeyword;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private String canonicalUrl;
    private Boolean robotsIndex;
    private Boolean robotsFollow;
    private String ogTitle;
    private String ogDescription;
    private String twitterTitle;
    private String twitterDescription;
    private String twitterImage;
    private String schemaType;
    private Integer seoScore;
    private Integer readabilityScore;

    private String status;
    private Long viewCount;
    private CategoryResponse category;
    private AuthorDto author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorDto {
        private String id;
        private String fullName;
        private String email;
    }

    public static ArticleResponse from(Article article) {
        ArticleResponseBuilder builder = ArticleResponse.builder()
                .id(article.getId().toString())
                .title(article.getTitle())
                .slug(article.getSlug())
                .summary(article.getSummary())
                .content(article.getContent())
                .contentHtml(article.getContentHtml())
                .featuredImage(article.getFeaturedImage())
                .featuredImageAlt(article.getFeaturedImageAlt())
                .featuredImageCaption(article.getFeaturedImageCaption())
                .galleryImages(article.getGalleryImages())
                .ogImage(article.getOgImage())
                .ogImageAlt(article.getOgImageAlt())
                .tags(article.getTags())
                .readingTime(article.getReadingTime())
                .isFeatured(article.getIsFeatured())
                .allowComments(article.getAllowComments())
                .publishedAt(article.getPublishedAt())
                .sourceUrl(article.getSourceUrl())
                .focusKeyword(article.getFocusKeyword())
                .metaTitle(article.getMetaTitle())
                .metaDescription(article.getMetaDescription())
                .metaKeywords(article.getMetaKeywords())
                .canonicalUrl(article.getCanonicalUrl())
                .robotsIndex(article.getRobotsIndex())
                .robotsFollow(article.getRobotsFollow())
                .ogTitle(article.getOgTitle())
                .ogDescription(article.getOgDescription())
                .twitterTitle(article.getTwitterTitle())
                .twitterDescription(article.getTwitterDescription())
                .twitterImage(article.getTwitterImage())
                .schemaType(article.getSchemaType())
                .seoScore(article.getSeoScore())
                .readabilityScore(article.getReadabilityScore())
                .status(article.getStatus().name())
                .viewCount(article.getViewCount())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt());

        if (article.getCategory() != null) {
            builder.category(CategoryResponse.from(article.getCategory()));
        }

        if (article.getAuthor() != null) {
            builder.author(AuthorDto.builder()
                    .id(article.getAuthor().getId().toString())
                    .fullName(article.getAuthor().getFullName())
                    .email(article.getAuthor().getEmail())
                    .build());
        }

        return builder.build();
    }
}
