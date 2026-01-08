package com.tree.controller;

import com.tree.dto.ApiResponse;
import com.tree.dto.PageResponse;
import com.tree.dto.product.ProductRequest;
import com.tree.dto.product.ProductResponse;
import com.tree.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Products", description = "Product management endpoints")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getAll(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(productService.getAllProducts(pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getById(id)));
    }

    @PostMapping
    @Operation(summary = "Create product")
    public ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productService.create(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete product (move to trash)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        productService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Product moved to trash", null));
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getFeatured() {
        return ResponseEntity.ok(ApiResponse.success(productService.getFeaturedProducts()));
    }

    // ==================== TRASH Operations ====================

    @GetMapping("/trash")
    @Operation(summary = "Get deleted products (trash)")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getTrash(
            @PageableDefault(size = 10, sort = "deletedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(productService.getDeletedProducts(pageable)));
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore product from trash")
    public ResponseEntity<ApiResponse<ProductResponse>> restore(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Product restored", productService.restore(id)));
    }

    @DeleteMapping("/{id}/permanent")
    @Operation(summary = "Permanently delete product")
    public ResponseEntity<ApiResponse<Void>> permanentDelete(@PathVariable UUID id) {
        productService.permanentDelete(id);
        return ResponseEntity.ok(ApiResponse.success("Product permanently deleted", null));
    }
}
