package com.tree.dto.product;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Name is required")
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
    private UUID categoryId;
}
