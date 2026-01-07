package com.tree.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerDTO {
    private UUID id;
    private String title;
    private String subtitle;
    private String buttonText;
    private String buttonLink;
    private String imageUrl;
    private List<SlideItem> slides; // Parsed from slidesJson
    private String bannerType; // HERO or EDITORIAL
    private String labelText; // For editorial banner label
    private Integer sortOrder;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlideItem {
        private String imageUrl;
        private String title;
        private String subtitle;
        private String buttonText;
        private String buttonLink;
    }
}
