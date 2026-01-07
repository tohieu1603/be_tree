package com.tree.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "banners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Banner extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String subtitle;

    @Column(name = "button_text")
    private String buttonText;

    @Column(name = "button_link")
    private String buttonLink;

    @Column(name = "image_url")
    private String imageUrl;

    // JSON array of slide objects for carousel
    // Each slide: { imageUrl, title, subtitle, buttonText, buttonLink }
    @Column(name = "slides_json", columnDefinition = "TEXT")
    private String slidesJson;

    // Legacy: JSON array of image URLs (for backward compatibility)
    @Column(name = "images_json", columnDefinition = "TEXT")
    private String imagesJson;

    // Banner type: HERO (main slideshow), EDITORIAL (mid-page banner)
    @Column(name = "banner_type")
    private String bannerType = "HERO";

    // Label text for editorial banner (e.g., "VONG TAY")
    @Column(name = "label_text")
    private String labelText;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(nullable = false)
    private Boolean active = true;
}
