package com.tree.service;

import com.tree.dto.category.CategoryRequest;
import com.tree.dto.category.CategoryResponse;
import com.tree.entity.Category;
import com.tree.exception.BadRequestException;
import com.tree.exception.ResourceNotFoundException;
import com.tree.repository.CategoryRepository;
import com.tree.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Get all categories as flat list
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .toList();
    }

    // Get categories as tree structure (only root categories with nested children)
    public List<CategoryResponse> getCategoryTree() {
        return categoryRepository.findByParentIsNullOrderBySortOrderAsc().stream()
                .map(CategoryResponse::fromWithChildren)
                .toList();
    }

    // Get active categories as tree structure
    public List<CategoryResponse> getActiveCategoryTree() {
        return categoryRepository.findByParentIsNullAndActiveTrueOrderBySortOrderAsc().stream()
                .map(CategoryResponse::fromWithChildren)
                .toList();
    }

    public List<CategoryResponse> getActiveCategories() {
        return categoryRepository.findByActiveOrderBySortOrderAsc(true).stream()
                .map(CategoryResponse::from)
                .toList();
    }

    public CategoryResponse getById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return CategoryResponse.fromWithChildren(category);
    }

    public CategoryResponse getBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "slug", slug));
        return CategoryResponse.fromWithChildren(category);
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        log.info("Creating category: {}", request.getName());

        String slug = request.getSlug();
        if (slug == null || slug.isBlank()) {
            slug = SlugUtil.generateUniqueSlug(request.getName(), categoryRepository::existsBySlug);
        } else if (categoryRepository.existsBySlug(slug)) {
            throw new BadRequestException("Slug already exists");
        }

        Category parent = null;
        if (request.getParentId() != null && !request.getParentId().isBlank()) {
            parent = categoryRepository.findById(UUID.fromString(request.getParentId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Parent Category", "id", request.getParentId()));
        }

        Category category = Category.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .icon(request.getIcon())
                .imageUrl(request.getImageUrl())
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .active(request.getActive() != null ? request.getActive() : true)
                .parent(parent)
                .build();

        categoryRepository.save(category);
        log.info("Category created: {}", category.getId());

        return CategoryResponse.from(category);
    }

    @Transactional
    public CategoryResponse update(UUID id, CategoryRequest request) {
        log.info("Updating category: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        category.setName(request.getName());

        if (request.getSlug() != null && !request.getSlug().equals(category.getSlug())) {
            if (categoryRepository.existsBySlug(request.getSlug())) {
                throw new BadRequestException("Slug already exists");
            }
            category.setSlug(request.getSlug());
        }

        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getIcon() != null) {
            category.setIcon(request.getIcon());
        }
        if (request.getImageUrl() != null) {
            category.setImageUrl(request.getImageUrl());
        }
        if (request.getSortOrder() != null) {
            category.setSortOrder(request.getSortOrder());
        }
        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }

        // Update parent
        if (request.getParentId() != null) {
            if (request.getParentId().isBlank()) {
                category.setParent(null);
            } else {
                UUID parentId = UUID.fromString(request.getParentId());
                // Prevent setting itself as parent
                if (parentId.equals(id)) {
                    throw new BadRequestException("Category cannot be its own parent");
                }
                Category parent = categoryRepository.findById(parentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Parent Category", "id", request.getParentId()));
                // Prevent circular reference
                if (isDescendant(category, parent)) {
                    throw new BadRequestException("Cannot set a descendant as parent (circular reference)");
                }
                category.setParent(parent);
            }
        }

        categoryRepository.save(category);
        log.info("Category updated: {}", id);

        return CategoryResponse.from(category);
    }

    // Check if potentialParent is a descendant of category
    private boolean isDescendant(Category category, Category potentialParent) {
        Category current = potentialParent;
        while (current != null) {
            if (current.getId().equals(category.getId())) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    @Transactional
    public void delete(UUID id) {
        log.info("Deleting category: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        // Check if has children
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            throw new BadRequestException("Cannot delete category with subcategories. Delete subcategories first.");
        }

        categoryRepository.deleteById(id);
        log.info("Category deleted: {}", id);
    }
}
