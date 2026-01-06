package com.tree.controller;

import com.tree.dto.ApiResponse;
import com.tree.dto.BannerDTO;
import com.tree.dto.PageResponse;
import com.tree.dto.SiteSettingsDTO;
import com.tree.dto.article.ArticleResponse;
import com.tree.dto.category.CategoryResponse;
import com.tree.dto.product.ProductResponse;
import com.tree.dto.seo.SeoSettingsResponse;
import com.tree.service.ArticleService;
import com.tree.service.BannerService;
import com.tree.service.CategoryService;
import com.tree.service.ProductService;
import com.tree.service.SeoSettingsService;
import com.tree.service.SiteSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Tag(name = "Public", description = "Public endpoints")
public class PublicController {

    private final ArticleService articleService;
    private final BannerService bannerService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final SeoSettingsService seoSettingsService;
    private final SiteSettingsService siteSettingsService;

    @GetMapping("/site-settings")
    @Operation(summary = "Get site settings")
    public ResponseEntity<ApiResponse<SiteSettingsDTO>> getSiteSettings() {
        return ResponseEntity.ok(ApiResponse.success(siteSettingsService.getSettings()));
    }

    @GetMapping("/banners")
    @Operation(summary = "Get active banners")
    public ResponseEntity<ApiResponse<List<BannerDTO>>> getBanners() {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getActiveBanners()));
    }

    @GetMapping("/articles")
    @Operation(summary = "Get published articles")
    public ResponseEntity<ApiResponse<PageResponse<ArticleResponse>>> getArticles(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(articleService.getPublishedArticles(pageable)));
    }

    @GetMapping("/articles/{slug}")
    @Operation(summary = "Get article by slug")
    public ResponseEntity<ApiResponse<ArticleResponse>> getArticle(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(articleService.getBySlug(slug)));
    }

    @GetMapping("/articles/search")
    @Operation(summary = "Search articles")
    public ResponseEntity<ApiResponse<PageResponse<ArticleResponse>>> searchArticles(
            @RequestParam String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(articleService.searchArticles(keyword, pageable)));
    }

    @GetMapping("/categories")
    @Operation(summary = "Get active categories as flat list")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getActiveCategories()));
    }

    @GetMapping("/categories/tree")
    @Operation(summary = "Get active categories as tree structure")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategoryTree() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getActiveCategoryTree()));
    }

    @GetMapping("/categories/{slug}")
    @Operation(summary = "Get category by slug")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getBySlug(slug)));
    }

    @GetMapping("/categories/{id}/articles")
    @Operation(summary = "Get articles by category")
    public ResponseEntity<ApiResponse<PageResponse<ArticleResponse>>> getArticlesByCategory(
            @PathVariable UUID id,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(articleService.getArticlesByCategory(id, pageable)));
    }

    @GetMapping("/seo")
    @Operation(summary = "Get SEO settings for sitemap/robots")
    public ResponseEntity<ApiResponse<SeoSettingsResponse>> getSeoSettings() {
        return ResponseEntity.ok(ApiResponse.success(seoSettingsService.getSettings()));
    }

    // Product endpoints
    @GetMapping("/products")
    @Operation(summary = "Get active products")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProducts(
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(productService.getActiveProducts(pageable)));
    }

    @GetMapping("/products/{slug}")
    @Operation(summary = "Get product by slug")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(productService.getBySlug(slug)));
    }

    @GetMapping("/products/search")
    @Operation(summary = "Search products")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> searchProducts(
            @RequestParam String keyword,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(productService.searchProducts(keyword, pageable)));
    }

    @GetMapping("/products/featured")
    @Operation(summary = "Get featured products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getFeaturedProducts() {
        return ResponseEntity.ok(ApiResponse.success(productService.getFeaturedProducts()));
    }

    @GetMapping("/products/category/{categoryId}")
    @Operation(summary = "Get products by category")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProductsByCategory(
            @PathVariable UUID categoryId,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductsByCategory(categoryId, pageable)));
    }

    @GetMapping("/products/{slug}/related")
    @Operation(summary = "Get related products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getRelatedProducts(@PathVariable String slug) {
        ProductResponse product = productService.getBySlug(slug);
        if (product.getCategory() != null) {
            return ResponseEntity.ok(ApiResponse.success(
                productService.getRelatedProducts(UUID.fromString(product.getCategory().getId()), UUID.fromString(product.getId()))));
        }
        return ResponseEntity.ok(ApiResponse.success(List.of()));
    }
}
