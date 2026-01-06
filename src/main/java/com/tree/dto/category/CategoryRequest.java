package com.tree.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String slug;
    private String description;
    private String icon;
    private String imageUrl;
    private Integer sortOrder;
    private Boolean active;
    private String parentId;
}
