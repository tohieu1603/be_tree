package com.tree.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "featured_image")
    private String featuredImage;

    // Multiple images stored as comma-separated URLs
    @Column(columnDefinition = "TEXT")
    private String images;

    @Column(precision = 15, scale = 0)
    private BigDecimal price;

    @Column(name = "original_price", precision = 15, scale = 0)
    private BigDecimal originalPrice;

    @Column(name = "sku")
    private String sku;

    // Dimensions
    @Column
    private String dimensions;

    // Material
    @Column
    private String material;

    // Color
    @Column
    private String color;

    // Weight in kg
    @Column
    private Double weight;

    // Stock quantity
    @Builder.Default
    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;

    @Builder.Default
    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    // SEO fields
    @Column(name = "meta_title")
    private String metaTitle;

    @Column(name = "meta_description")
    private String metaDescription;

    @Column(name = "meta_keywords")
    private String metaKeywords;

    @Column(name = "view_count")
    @Builder.Default
    private Long viewCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
