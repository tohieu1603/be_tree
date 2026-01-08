package com.tree.service;

import com.tree.dto.PageResponse;
import com.tree.dto.category.CategoryResponse;
import com.tree.dto.product.ProductRequest;
import com.tree.dto.product.ProductResponse;
import com.tree.entity.Category;
import com.tree.entity.Product;
import com.tree.exception.ResourceNotFoundException;
import com.tree.repository.CategoryRepository;
import com.tree.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.tree.util.ServiceUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private static final String ENTITY_NAME = "Product";

    // ==================== READ Operations ====================

    public PageResponse<ProductResponse> getAllProducts(Pageable pageable) {
        return mapPage(productRepository.findByDeletedFalse(pageable));
    }

    public PageResponse<ProductResponse> getActiveProducts(Pageable pageable) {
        return mapPage(productRepository.findByIsActiveTrueAndDeletedFalse(pageable));
    }

    public PageResponse<ProductResponse> getProductsByCategory(UUID categoryId, Pageable pageable) {
        return mapPage(productRepository.findByCategoryIdAndIsActiveTrueAndDeletedFalse(categoryId, pageable));
    }

    public PageResponse<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        return mapPage(productRepository.searchProducts(keyword, pageable));
    }

    public List<ProductResponse> getFeaturedProducts() {
        return mapList(productRepository.findByIsFeaturedTrueAndIsActiveTrueAndDeletedFalse());
    }

    public List<ProductResponse> getRelatedProducts(UUID categoryId, UUID productId) {
        return mapList(productRepository.findRelatedProducts(categoryId, productId, PageRequest.of(0, 4)));
    }

    public ProductResponse getById(UUID id) {
        return toResponse(findByIdOrThrow(productRepository::findById, id, ENTITY_NAME));
    }

    @Transactional
    public ProductResponse getBySlug(String slug) {
        Product product = findOrThrow(productRepository.findBySlugAndDeletedFalse(slug), ENTITY_NAME, "slug", slug);
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
        return toResponse(product);
    }

    // ==================== TRASH Operations ====================

    public PageResponse<ProductResponse> getDeletedProducts(Pageable pageable) {
        return mapPage(productRepository.findByDeletedTrue(pageable));
    }

    // ==================== WRITE Operations ====================

    @Transactional
    public ProductResponse create(ProductRequest request) {
        log.info("Creating product: {}", request.getName());

        String slug = validateOrGenerateSlug(request.getSlug(), request.getName(), productRepository::existsBySlug);

        Product product = Product.builder()
                .name(request.getName())
                .slug(slug)
                .summary(request.getSummary())
                .description(request.getDescription())
                .featuredImage(request.getFeaturedImage())
                .images(joinImages(request.getImages()))
                .price(request.getPrice())
                .originalPrice(request.getOriginalPrice())
                .sku(request.getSku())
                .dimensions(request.getDimensions())
                .material(request.getMaterial())
                .color(request.getColor())
                .weight(request.getWeight())
                .stockQuantity(getOrDefault(request.getStockQuantity(), 0))
                .isFeatured(getOrDefault(request.getIsFeatured(), false))
                .isActive(getOrDefault(request.getIsActive(), true))
                .metaTitle(getOrDefault(request.getMetaTitle(), request.getName()))
                .metaDescription(request.getMetaDescription())
                .metaKeywords(request.getMetaKeywords())
                .viewCount(0L)
                .build();

        setCategory(product, request.getCategoryId());
        productRepository.save(product);

        log.info("Product created: {}", product.getId());
        return toResponse(product);
    }

    @Transactional
    public ProductResponse update(UUID id, ProductRequest request) {
        log.info("Updating product: {}", id);

        Product product = findByIdOrThrow(productRepository::findById, id, ENTITY_NAME);

        product.setName(request.getName());
        validateSlugOnUpdate(request.getSlug(), product.getSlug(), productRepository::existsBySlug);
        updateIfNotNull(request.getSlug(), product::setSlug);

        product.setSummary(request.getSummary());
        product.setDescription(request.getDescription());
        product.setFeaturedImage(request.getFeaturedImage());
        product.setImages(joinImages(request.getImages()));
        product.setPrice(request.getPrice());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setSku(request.getSku());
        product.setDimensions(request.getDimensions());
        product.setMaterial(request.getMaterial());
        product.setColor(request.getColor());
        product.setWeight(request.getWeight());

        updateIfNotNull(request.getStockQuantity(), product::setStockQuantity);
        updateIfNotNull(request.getIsFeatured(), product::setIsFeatured);
        updateIfNotNull(request.getIsActive(), product::setIsActive);

        product.setMetaTitle(request.getMetaTitle());
        product.setMetaDescription(request.getMetaDescription());
        product.setMetaKeywords(request.getMetaKeywords());

        if (request.getCategoryId() != null) {
            setCategory(product, request.getCategoryId());
        } else {
            product.setCategory(null);
        }

        productRepository.save(product);
        log.info("Product updated: {}", id);
        return toResponse(product);
    }

    @Transactional
    public void delete(UUID id) {
        log.info("Soft deleting product: {}", id);
        Product product = findByIdOrThrow(productRepository::findById, id, ENTITY_NAME);
        product.setDeleted(true);
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
        log.info("Product soft deleted: {}", id);
    }

    @Transactional
    public ProductResponse restore(UUID id) {
        log.info("Restoring product: {}", id);
        Product product = findByIdOrThrow(productRepository::findById, id, ENTITY_NAME);
        product.setDeleted(false);
        product.setDeletedAt(null);
        productRepository.save(product);
        log.info("Product restored: {}", id);
        return toResponse(product);
    }

    @Transactional
    public void permanentDelete(UUID id) {
        log.info("Permanently deleting product: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException(ENTITY_NAME, "id", id);
        }
        productRepository.deleteById(id);
        log.info("Product permanently deleted: {}", id);
    }

    // ==================== Helper Methods ====================

    private void setCategory(Product product, UUID categoryId) {
        if (categoryId != null) {
            Category category = findByIdOrThrow(categoryRepository::findById, categoryId, "Category");
            product.setCategory(category);
        }
    }

    private String joinImages(List<String> images) {
        return images != null ? String.join(",", images) : null;
    }

    private List<String> splitImages(String images) {
        return images != null && !images.isBlank()
                ? Arrays.asList(images.split(","))
                : Collections.emptyList();
    }

    private PageResponse<ProductResponse> mapPage(Page<Product> page) {
        return PageResponse.from(page.map(this::toResponse));
    }

    private List<ProductResponse> mapList(List<Product> products) {
        return products.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId().toString())
                .name(product.getName())
                .slug(product.getSlug())
                .summary(product.getSummary())
                .description(product.getDescription())
                .featuredImage(product.getFeaturedImage())
                .images(splitImages(product.getImages()))
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
                .deleted(product.getDeleted())
                .deletedAt(product.getDeletedAt())
                .build();
    }
}
