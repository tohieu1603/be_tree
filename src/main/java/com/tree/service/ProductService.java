package com.tree.service;

import com.tree.dto.PageResponse;
import com.tree.dto.category.CategoryResponse;
import com.tree.dto.product.ProductRequest;
import com.tree.dto.product.ProductResponse;
import com.tree.entity.Category;
import com.tree.entity.Product;
import com.tree.exception.BadRequestException;
import com.tree.exception.ResourceNotFoundException;
import com.tree.repository.CategoryRepository;
import com.tree.repository.ProductRepository;
import com.tree.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public PageResponse<ProductResponse> getAllProducts(Pageable pageable) {
        Page<ProductResponse> page = productRepository.findAll(pageable)
                .map(this::toResponse);
        return PageResponse.from(page);
    }

    public PageResponse<ProductResponse> getActiveProducts(Pageable pageable) {
        Page<ProductResponse> page = productRepository.findByIsActiveTrue(pageable)
                .map(this::toResponse);
        return PageResponse.from(page);
    }

    public PageResponse<ProductResponse> getProductsByCategory(UUID categoryId, Pageable pageable) {
        Page<ProductResponse> page = productRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable)
                .map(this::toResponse);
        return PageResponse.from(page);
    }

    public PageResponse<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        Page<ProductResponse> page = productRepository.searchProducts(keyword, pageable)
                .map(this::toResponse);
        return PageResponse.from(page);
    }

    public List<ProductResponse> getFeaturedProducts() {
        return productRepository.findByIsFeaturedTrueAndIsActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getRelatedProducts(UUID categoryId, UUID productId) {
        return productRepository.findRelatedProducts(categoryId, productId, PageRequest.of(0, 4))
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return toResponse(product);
    }

    @Transactional
    public ProductResponse getBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "slug", slug));
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
        return toResponse(product);
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        log.info("Creating product: {}", request.getName());

        String slug = request.getSlug();
        if (slug == null || slug.isBlank()) {
            slug = SlugUtil.generateUniqueSlug(request.getName(), productRepository::existsBySlug);
        } else if (productRepository.existsBySlug(slug)) {
            throw new BadRequestException("Slug already exists");
        }

        Product product = Product.builder()
                .name(request.getName())
                .slug(slug)
                .summary(request.getSummary())
                .description(request.getDescription())
                .featuredImage(request.getFeaturedImage())
                .images(request.getImages() != null ? String.join(",", request.getImages()) : null)
                .price(request.getPrice())
                .originalPrice(request.getOriginalPrice())
                .sku(request.getSku())
                .dimensions(request.getDimensions())
                .material(request.getMaterial())
                .color(request.getColor())
                .weight(request.getWeight())
                .stockQuantity(request.getStockQuantity() != null ? request.getStockQuantity() : 0)
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .metaTitle(request.getMetaTitle() != null ? request.getMetaTitle() : request.getName())
                .metaDescription(request.getMetaDescription())
                .metaKeywords(request.getMetaKeywords())
                .viewCount(0L)
                .build();

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            product.setCategory(category);
        }

        productRepository.save(product);
        log.info("Product created: {}", product.getId());

        return toResponse(product);
    }

    @Transactional
    public ProductResponse update(UUID id, ProductRequest request) {
        log.info("Updating product: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setName(request.getName());

        if (request.getSlug() != null && !request.getSlug().equals(product.getSlug())) {
            if (productRepository.existsBySlug(request.getSlug())) {
                throw new BadRequestException("Slug already exists");
            }
            product.setSlug(request.getSlug());
        }

        product.setSummary(request.getSummary());
        product.setDescription(request.getDescription());
        product.setFeaturedImage(request.getFeaturedImage());
        product.setImages(request.getImages() != null ? String.join(",", request.getImages()) : null);
        product.setPrice(request.getPrice());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setSku(request.getSku());
        product.setDimensions(request.getDimensions());
        product.setMaterial(request.getMaterial());
        product.setColor(request.getColor());
        product.setWeight(request.getWeight());

        if (request.getStockQuantity() != null) {
            product.setStockQuantity(request.getStockQuantity());
        }
        if (request.getIsFeatured() != null) {
            product.setIsFeatured(request.getIsFeatured());
        }
        if (request.getIsActive() != null) {
            product.setIsActive(request.getIsActive());
        }

        product.setMetaTitle(request.getMetaTitle());
        product.setMetaDescription(request.getMetaDescription());
        product.setMetaKeywords(request.getMetaKeywords());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        productRepository.save(product);
        log.info("Product updated: {}", id);

        return toResponse(product);
    }

    @Transactional
    public void delete(UUID id) {
        log.info("Deleting product: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        productRepository.deleteById(id);
        log.info("Product deleted: {}", id);
    }

    private ProductResponse toResponse(Product product) {
        List<String> images = product.getImages() != null && !product.getImages().isBlank()
                ? Arrays.asList(product.getImages().split(","))
                : Collections.emptyList();

        return ProductResponse.builder()
                .id(product.getId().toString())
                .name(product.getName())
                .slug(product.getSlug())
                .summary(product.getSummary())
                .description(product.getDescription())
                .featuredImage(product.getFeaturedImage())
                .images(images)
                .price(product.getPrice())
                .originalPrice(product.getOriginalPrice())
                .sku(product.getSku())
                .dimensions(product.getDimensions())
                .material(product.getMaterial())
                .color(product.getColor())
                .weight(product.getWeight())
                .stockQuantity(product.getStockQuantity())
                .isFeatured(product.getIsFeatured())
                .isActive(product.getIsActive())
                .metaTitle(product.getMetaTitle())
                .metaDescription(product.getMetaDescription())
                .metaKeywords(product.getMetaKeywords())
                .viewCount(product.getViewCount())
                .category(product.getCategory() != null ? CategoryResponse.from(product.getCategory()) : null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
