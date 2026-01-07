package com.tree.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tree.dto.BannerDTO;
import com.tree.entity.Banner;
import com.tree.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;
    private final ObjectMapper objectMapper;

    public List<BannerDTO> getAllBanners() {
        return bannerRepository.findAllByOrderBySortOrderAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<BannerDTO> getActiveBanners() {
        return bannerRepository.findByActiveTrueOrderBySortOrderAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<BannerDTO> getActiveBannersByType(String bannerType) {
        return bannerRepository.findByActiveTrueAndBannerTypeOrderBySortOrderAsc(bannerType)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public BannerDTO getBannerById(UUID id) {
        return bannerRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Banner not found"));
    }

    @Transactional
    public BannerDTO createBanner(BannerDTO dto) {
        Banner banner = new Banner();
        updateBannerFromDTO(banner, dto);
        return toDTO(bannerRepository.save(banner));
    }

    @Transactional
    public BannerDTO updateBanner(UUID id, BannerDTO dto) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner not found"));
        updateBannerFromDTO(banner, dto);
        return toDTO(bannerRepository.save(banner));
    }

    @Transactional
    public void deleteBanner(UUID id) {
        bannerRepository.deleteById(id);
    }

    private void updateBannerFromDTO(Banner banner, BannerDTO dto) {
        banner.setTitle(dto.getTitle());
        banner.setSubtitle(dto.getSubtitle());
        banner.setButtonText(dto.getButtonText());
        banner.setButtonLink(dto.getButtonLink());
        banner.setImageUrl(dto.getImageUrl());
        banner.setBannerType(dto.getBannerType() != null ? dto.getBannerType() : "HERO");
        banner.setLabelText(dto.getLabelText());
        banner.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        banner.setActive(dto.getActive() != null ? dto.getActive() : true);

        // Convert slides list to JSON
        if (dto.getSlides() != null && !dto.getSlides().isEmpty()) {
            try {
                banner.setSlidesJson(objectMapper.writeValueAsString(dto.getSlides()));
            } catch (JsonProcessingException e) {
                banner.setSlidesJson("[]");
            }
        } else {
            banner.setSlidesJson(null);
        }
    }

    private BannerDTO toDTO(Banner banner) {
        BannerDTO dto = new BannerDTO();
        dto.setId(banner.getId());
        dto.setTitle(banner.getTitle());
        dto.setSubtitle(banner.getSubtitle());
        dto.setButtonText(banner.getButtonText());
        dto.setButtonLink(banner.getButtonLink());
        dto.setImageUrl(banner.getImageUrl());
        dto.setBannerType(banner.getBannerType() != null ? banner.getBannerType() : "HERO");
        dto.setLabelText(banner.getLabelText());
        dto.setSortOrder(banner.getSortOrder());
        dto.setActive(banner.getActive());
        dto.setCreatedAt(banner.getCreatedAt());
        dto.setUpdatedAt(banner.getUpdatedAt());

        // Parse slides from JSON - with backward compatibility for old imagesJson format
        List<BannerDTO.SlideItem> slides = new ArrayList<>();

        if (banner.getSlidesJson() != null && !banner.getSlidesJson().isEmpty()) {
            // New format: slides with full content
            try {
                slides = objectMapper.readValue(banner.getSlidesJson(), new TypeReference<List<BannerDTO.SlideItem>>() {});
            } catch (JsonProcessingException e) {
                // ignore
            }
        }

        // Backward compatibility: convert old imagesJson (list of URLs) to slides
        if (slides.isEmpty() && banner.getImagesJson() != null && !banner.getImagesJson().isEmpty()) {
            try {
                List<String> imageUrls = objectMapper.readValue(banner.getImagesJson(), new TypeReference<List<String>>() {});
                for (String imageUrl : imageUrls) {
                    BannerDTO.SlideItem slide = new BannerDTO.SlideItem();
                    slide.setImageUrl(imageUrl);
                    slide.setTitle(banner.getTitle());
                    slide.setSubtitle(banner.getSubtitle());
                    slide.setButtonText(banner.getButtonText());
                    slide.setButtonLink(banner.getButtonLink());
                    slides.add(slide);
                }
            } catch (JsonProcessingException e) {
                // ignore
            }
        }

        // If still empty but has imageUrl, create single slide from banner fields
        if (slides.isEmpty() && banner.getImageUrl() != null && !banner.getImageUrl().isEmpty()) {
            BannerDTO.SlideItem slide = new BannerDTO.SlideItem();
            slide.setImageUrl(banner.getImageUrl());
            slide.setTitle(banner.getTitle());
            slide.setSubtitle(banner.getSubtitle());
            slide.setButtonText(banner.getButtonText());
            slide.setButtonLink(banner.getButtonLink());
            slides.add(slide);
        }

        dto.setSlides(slides);

        return dto;
    }
}
