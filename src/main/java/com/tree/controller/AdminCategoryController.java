package com.tree.controller;

import com.tree.dto.ApiResponse;
import com.tree.dto.category.CategoryRequest;
import com.tree.dto.category.CategoryResponse;
import com.tree.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@Tag(name = "Admin - Categories", description = "Category management")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories as flat list")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getAllCategories()));
    }

    @GetMapping("/tree")
    @Operation(summary = "Get categories as tree structure")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getTree() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getCategoryTree()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getById(id)));
    }

    @PostMapping
    @Operation(summary = "Create category")
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Category created", categoryService.create(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Category updated", categoryService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category (soft delete)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Danh mục đã được chuyển vào thùng rác", null));
    }

    // Trash endpoints
    @GetMapping("/trash")
    @Operation(summary = "Get all deleted categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getTrash() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getTrashCategories()));
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore category from trash")
    public ResponseEntity<ApiResponse<CategoryResponse>> restore(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Danh mục đã được khôi phục", categoryService.restore(id)));
    }

    @DeleteMapping("/{id}/permanent")
    @Operation(summary = "Permanently delete category")
    public ResponseEntity<ApiResponse<Void>> permanentDelete(@PathVariable UUID id) {
        categoryService.permanentDelete(id);
        return ResponseEntity.ok(ApiResponse.success("Danh mục đã được xoá vĩnh viễn", null));
    }
}
