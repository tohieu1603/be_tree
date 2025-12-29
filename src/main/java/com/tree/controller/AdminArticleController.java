package com.tree.controller;

import com.tree.dto.ApiResponse;
import com.tree.dto.PageResponse;
import com.tree.dto.article.ArticleRequest;
import com.tree.dto.article.ArticleResponse;
import com.tree.security.UserPrincipal;
import com.tree.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/articles")
@RequiredArgsConstructor
@Tag(name = "Admin - Articles", description = "Article management")
public class AdminArticleController {

    private final ArticleService articleService;

    @GetMapping
    @Operation(summary = "Get all articles (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<ArticleResponse>>> getAll(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(articleService.getAllArticles(pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get article by ID")
    public ResponseEntity<ApiResponse<ArticleResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(articleService.getById(id)));
    }

    @PostMapping
    @Operation(summary = "Create article")
    public ResponseEntity<ApiResponse<ArticleResponse>> create(
            @Valid @RequestBody ArticleRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(ApiResponse.success("Article created",
                articleService.create(request, user.getId())));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update article")
    public ResponseEntity<ApiResponse<ArticleResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ArticleRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Article updated", articleService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete article")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        articleService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Article deleted", null));
    }

    @PostMapping("/convert-html")
    @Operation(summary = "Convert HTML to Markdown")
    public ResponseEntity<ApiResponse<Map<String, String>>> convertHtmlToMarkdown(@RequestBody Map<String, String> request) {
        String html = request.get("html");
        String markdown = articleService.convertHtmlToMarkdown(html);
        return ResponseEntity.ok(ApiResponse.success(Map.of("markdown", markdown)));
    }
}
