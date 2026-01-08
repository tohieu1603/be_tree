package com.tree.dto.category;

import com.tree.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private String id;
    private String name;
    private String slug;
    private String description;
    private String icon;
    private String imageUrl;
    private String sectionTitle;
    private String sectionSubtitle;
    private Integer sortOrder;
    private boolean active;
    private int articleCount;
    private String parentId;
    private String parentName;
    private int level;
    @Builder.Default
    private List<CategoryResponse> children = new ArrayList<>();
    private LocalDateTime createdAt;
    private Boolean deleted;
    private LocalDateTime deletedAt;

    // Simple conversion without children (for flat list)
    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .id(category.getId().toString())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .icon(category.getIcon())
                .imageUrl(category.getImageUrl())
                .sectionTitle(category.getSectionTitle())
                .sectionSubtitle(category.getSectionSubtitle())
                .sortOrder(category.getSortOrder())
                .active(category.isActive())
                .articleCount(category.getArticles() != null ? (int) category.getArticles().stream().filter(a -> !a.getDeleted()).count() : 0)
                .parentId(category.getParent() != null ? category.getParent().getId().toString() : null)
                .parentName(category.getParent() != null ? category.getParent().getName() : null)
                .level(category.getLevel())
                .createdAt(category.getCreatedAt())
                .deleted(category.getDeleted())
                .deletedAt(category.getDeletedAt())
                .build();
    }

    // Recursive conversion with children (for tree structure)
    public static CategoryResponse fromWithChildren(Category category) {
        CategoryResponse response = from(category);
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            response.setChildren(category.getChildren().stream()
                    .map(CategoryResponse::fromWithChildren)
                    .toList());
        }
        return response;
    }
}
