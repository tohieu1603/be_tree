package com.tree.service;

import com.tree.dto.category.CategoryRequest;
import com.tree.dto.category.CategoryResponse;
import com.tree.entity.Category;
import com.tree.exception.BadRequestException;
import com.tree.exception.ResourceNotFoundException;
import com.tree.entity.Product;
import com.tree.repository.CategoryRepository;
import com.tree.repository.ArticleRepository;
import com.tree.repository.ProductRepository;
import com.tree.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;
    private final ProductRepository productRepository;

    // Get all categories as flat list (non-deleted)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findByDeletedFalseOrderBySortOrderAsc().stream()
                .map(CategoryResponse::from)
                .toList();
    }

    // Get categories as tree structure (only root categories with nested children)
    public List<CategoryResponse> getCategoryTree() {
        return categoryRepository.findByParentIsNullAndDeletedFalseOrderBySortOrderAsc().stream()
                .map(CategoryResponse::fromWithChildren)
                .toList();
    }

    // Get active categories as tree structure
    public List<CategoryResponse> getActiveCategoryTree() {
        return categoryRepository.findByParentIsNullAndActiveTrueAndDeletedFalseOrderBySortOrderAsc().stream()
                .map(CategoryResponse::fromWithChildren)
                .toList();
    }

    public List<CategoryResponse> getActiveCategories() {
        return categoryRepository.findByActiveAndDeletedFalseOrderBySortOrderAsc(true).stream()
                .map(CategoryResponse::from)
                .toList();
    }

    // Trash methods
    public List<CategoryResponse> getTrashCategories() {
        return categoryRepository.findByDeletedTrueOrderByDeletedAtDesc().stream()
                .map(CategoryResponse::from)
                .toList();
    }

    public CategoryResponse getById(UUID id) {
        Category category = categoryRepository.findById(id)
                .filter(c -> !c.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return CategoryResponse.fromWithChildren(category);
    }

    public CategoryResponse getBySlug(String slug) {
        Category category = categoryRepository.findBySlugAndDeletedFalse(slug)
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
                .sectionTitle(request.getSectionTitle())
                .sectionSubtitle(request.getSectionSubtitle())
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
        if (request.getSectionTitle() != null) {
            category.setSectionTitle(request.getSectionTitle());
        }
        if (request.getSectionSubtitle() != null) {
            category.setSectionSubtitle(request.getSectionSubtitle());
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
        log.info("Soft deleting category: {}", id);

        Category category = categoryRepository.findById(id)
                .filter(c -> !c.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        // Check if has non-deleted children
        long activeChildren = category.getChildren() != null
                ? category.getChildren().stream().filter(c -> !c.getDeleted()).count()
                : 0;
        if (activeChildren > 0) {
            throw new BadRequestException("Không thể xoá danh mục có danh mục con. Xoá danh mục con trước.");
        }

        // Check if has non-deleted articles using repository count
        long articleCount = articleRepository.countByCategoryIdAndDeletedFalse(id);
        if (articleCount > 0) {
            throw new BadRequestException("Không thể xoá danh mục có " + articleCount + " bài viết. Di chuyển hoặc xoá bài viết trước.");
        }

        category.setDeleted(true);
        category.setDeletedAt(LocalDateTime.now());
        categoryRepository.save(category);
        log.info("Category soft deleted: {}", id);
    }

    @Transactional
    public CategoryResponse restore(UUID id) {
        log.info("Restoring category: {}", id);

        Category category = categoryRepository.findById(id)
                .filter(Category::getDeleted)
                .orElseThrow(() -> new ResourceNotFoundException("Category in trash", "id", id));

        category.setDeleted(false);
        category.setDeletedAt(null);
        categoryRepository.save(category);
        log.info("Category restored: {}", id);

        return CategoryResponse.from(category);
    }

    @Transactional
    public void permanentDelete(UUID id) {
        log.info("Permanently deleting category: {}", id);

        Category category = categoryRepository.findById(id)
                .filter(Category::getDeleted)
                .orElseThrow(() -> new ResourceNotFoundException("Category in trash", "id", id));

        // Detach articles from this category (set their category to null)
        if (category.getArticles() != null && !category.getArticles().isEmpty()) {
            category.getArticles().forEach(article -> article.setCategory(null));
            articleRepository.saveAll(category.getArticles());
        }

        // Detach products from this category (set their category to null)
        List<Product> products = productRepository.findByCategoryId(id);
        if (!products.isEmpty()) {
            products.forEach(product -> product.setCategory(null));
            productRepository.saveAll(products);
        }

        // Detach children from this category (set their parent to null)
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            category.getChildren().forEach(child -> child.setParent(null));
            categoryRepository.saveAll(category.getChildren());
        }

        // Flush all changes before delete
        categoryRepository.flush();

        // Use native delete to bypass JPA cascade/orphanRemoval issues
        categoryRepository.deleteByIdNative(id);

        log.info("Category permanently deleted: {}", id);
    }
}
