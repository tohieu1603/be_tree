package com.tree.dto.product;

import com.tree.dto.category.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private String slug;
    private String summary;
    private String description;
    private String featuredImage;
    private List<String> images;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String sku;
    private String dimensions;
    private String material;
    private String color;
    private Double weight;
    private Integer stockQuantity;
    private Boolean isFeatured;
    private Boolean isActive;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private Long viewCount;
    private CategoryResponse category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean deleted;
    private LocalDateTime deletedAt;
}
