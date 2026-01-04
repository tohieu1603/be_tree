package com.tree.service;

import com.tree.dto.PageResponse;
import com.tree.dto.article.ArticleRequest;
import com.tree.dto.article.ArticleResponse;
import com.tree.entity.Article;
import com.tree.entity.Article.Status;
import com.tree.entity.Category;
import com.tree.entity.User;
import com.tree.exception.BadRequestException;
import com.tree.exception.ResourceNotFoundException;
import com.tree.repository.ArticleRepository;
import com.tree.repository.CategoryRepository;
import com.tree.repository.UserRepository;
import com.tree.util.MarkdownUtil;
import com.tree.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final GoogleIndexingService googleIndexingService;

    public PageResponse<ArticleResponse> getAllArticles(Pageable pageable) {
        Page<ArticleResponse> page = articleRepository.findAll(pageable)
                .map(ArticleResponse::from);
        return PageResponse.from(page);
    }

    public PageResponse<ArticleResponse> getPublishedArticles(Pageable pageable) {
        Page<ArticleResponse> page = articleRepository.findByStatus(Status.PUBLISHED, pageable)
                .map(ArticleResponse::from);
        return PageResponse.from(page);
    }

    public PageResponse<ArticleResponse> getArticlesByCategory(UUID categoryId, Pageable pageable) {
        Page<ArticleResponse> page = articleRepository
                .findByCategoryIdAndStatus(categoryId, Status.PUBLISHED, pageable)
                .map(ArticleResponse::from);
        return PageResponse.from(page);
    }

    public PageResponse<ArticleResponse> searchArticles(String keyword, Pageable pageable) {
        Page<ArticleResponse> page = articleRepository
                .searchArticles(keyword, Status.PUBLISHED, pageable)
                .map(ArticleResponse::from);
        return PageResponse.from(page);
    }

    public ArticleResponse getById(UUID id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));
        return ArticleResponse.from(article);
    }

    @Transactional
    public ArticleResponse getBySlug(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "slug", slug));

        article.setViewCount(article.getViewCount() + 1);
        articleRepository.save(article);

        return ArticleResponse.from(article);
    }

    @Transactional
    public ArticleResponse create(ArticleRequest request, UUID authorId) {
        log.info("Creating article: {}", request.getTitle());

        String slug = request.getSlug();
        if (slug == null || slug.isBlank()) {
            slug = SlugUtil.generateUniqueSlug(request.getTitle(), articleRepository::existsBySlug);
        } else if (articleRepository.existsBySlug(slug)) {
            throw new BadRequestException("Slug already exists");
        }

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", authorId));

        // Calculate reading time (~200 words/minute)
        Integer readingTime = request.getReadingTime();
        if (readingTime == null && request.getContent() != null) {
            int wordCount = request.getContent().split("\\s+").length;
            readingTime = Math.max(1, wordCount / 200);
        }

        Article article = Article.builder()
                .title(request.getTitle())
                .slug(slug)
                .summary(request.getSummary())
                .content(request.getContent())
                .contentHtml(MarkdownUtil.markdownToHtml(request.getContent()))
                .featuredImage(request.getFeaturedImage())
                .featuredImageAlt(request.getFeaturedImageAlt())
                .featuredImageCaption(request.getFeaturedImageCaption())
                .galleryImages(request.getGalleryImages())
                .ogImage(request.getOgImage())
                .ogImageAlt(request.getOgImageAlt())
                .tags(request.getTags())
                .readingTime(readingTime)
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .allowComments(request.getAllowComments() != null ? request.getAllowComments() : true)
                .publishedAt(request.getPublishedAt())
                .sourceUrl(request.getSourceUrl())
                // SEO fields
                .focusKeyword(request.getFocusKeyword())
                .metaTitle(request.getMetaTitle() != null ? request.getMetaTitle() : request.getTitle())
                .metaDescription(request.getMetaDescription())
                .metaKeywords(request.getMetaKeywords())
                .canonicalUrl(request.getCanonicalUrl())
                .robotsIndex(request.getRobotsIndex() != null ? request.getRobotsIndex() : true)
                .robotsFollow(request.getRobotsFollow() != null ? request.getRobotsFollow() : true)
                .ogTitle(request.getOgTitle())
                .ogDescription(request.getOgDescription())
                .twitterTitle(request.getTwitterTitle())
                .twitterDescription(request.getTwitterDescription())
                .twitterImage(request.getTwitterImage())
                .schemaType(request.getSchemaType() != null ? request.getSchemaType() : "Article")
                .status(request.getStatus() != null ? Status.valueOf(request.getStatus()) : Status.DRAFT)
                .author(author)
                .viewCount(0L)
                .build();

        // Set publishedAt if publishing
        if (article.getStatus() == Status.PUBLISHED && article.getPublishedAt() == null) {
            article.setPublishedAt(LocalDateTime.now());
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(UUID.fromString(request.getCategoryId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            article.setCategory(category);
        }

        articleRepository.save(article);
        log.info("Article created: {}", article.getId());

        // Notify Google if published
        if (article.getStatus() == Status.PUBLISHED) {
            googleIndexingService.notifyArticlePublished(article.getSlug());
        }

        return ArticleResponse.from(article);
    }

    @Transactional
    public ArticleResponse update(UUID id, ArticleRequest request) {
        log.info("Updating article: {}", id);

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));

        article.setTitle(request.getTitle());

        if (request.getSlug() != null && !request.getSlug().equals(article.getSlug())) {
            if (articleRepository.existsBySlug(request.getSlug())) {
                throw new BadRequestException("Slug already exists");
            }
            article.setSlug(request.getSlug());
        }

        article.setSummary(request.getSummary());
        article.setContent(request.getContent());
        article.setContentHtml(MarkdownUtil.markdownToHtml(request.getContent()));
        article.setFeaturedImage(request.getFeaturedImage());
        article.setFeaturedImageAlt(request.getFeaturedImageAlt());
        article.setFeaturedImageCaption(request.getFeaturedImageCaption());
        article.setGalleryImages(request.getGalleryImages());
        article.setOgImage(request.getOgImage());
        article.setOgImageAlt(request.getOgImageAlt());
        article.setTags(request.getTags());
        article.setSourceUrl(request.getSourceUrl());

        // Update reading time
        if (request.getReadingTime() != null) {
            article.setReadingTime(request.getReadingTime());
        } else if (request.getContent() != null) {
            int wordCount = request.getContent().split("\\s+").length;
            article.setReadingTime(Math.max(1, wordCount / 200));
        }

        if (request.getIsFeatured() != null) {
            article.setIsFeatured(request.getIsFeatured());
        }
        if (request.getAllowComments() != null) {
            article.setAllowComments(request.getAllowComments());
        }
        if (request.getPublishedAt() != null) {
            article.setPublishedAt(request.getPublishedAt());
        }

        // SEO fields
        article.setFocusKeyword(request.getFocusKeyword());
        article.setMetaTitle(request.getMetaTitle());
        article.setMetaDescription(request.getMetaDescription());
        article.setMetaKeywords(request.getMetaKeywords());
        article.setCanonicalUrl(request.getCanonicalUrl());
        if (request.getRobotsIndex() != null) {
            article.setRobotsIndex(request.getRobotsIndex());
        }
        if (request.getRobotsFollow() != null) {
            article.setRobotsFollow(request.getRobotsFollow());
        }
        article.setOgTitle(request.getOgTitle());
        article.setOgDescription(request.getOgDescription());
        article.setTwitterTitle(request.getTwitterTitle());
        article.setTwitterDescription(request.getTwitterDescription());
        article.setTwitterImage(request.getTwitterImage());
        if (request.getSchemaType() != null) {
            article.setSchemaType(request.getSchemaType());
        }

        if (request.getStatus() != null) {
            Status newStatus = Status.valueOf(request.getStatus());
            // Set publishedAt when first publishing
            if (newStatus == Status.PUBLISHED && article.getStatus() != Status.PUBLISHED && article.getPublishedAt() == null) {
                article.setPublishedAt(LocalDateTime.now());
            }
            article.setStatus(newStatus);
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(UUID.fromString(request.getCategoryId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            article.setCategory(category);
        } else {
            article.setCategory(null);
        }

        articleRepository.save(article);
        log.info("Article updated: {}", id);

        // Notify Google if published
        if (article.getStatus() == Status.PUBLISHED) {
            googleIndexingService.notifyArticlePublished(article.getSlug());
        }

        return ArticleResponse.from(article);
    }

    @Transactional
    public void delete(UUID id) {
        log.info("Deleting article: {}", id);

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));

        String slug = article.getSlug();
        boolean wasPublished = article.getStatus() == Status.PUBLISHED;

        articleRepository.delete(article);
        log.info("Article deleted: {}", id);

        // Notify Google if was published
        if (wasPublished) {
            googleIndexingService.notifyArticleDeleted(slug);
        }
    }

    public String convertHtmlToMarkdown(String html) {
        return MarkdownUtil.htmlToMarkdown(html);
    }
}
