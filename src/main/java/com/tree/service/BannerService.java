package com.tree.service;

import com.tree.dto.BannerDTO;
import com.tree.entity.Banner;
import com.tree.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

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
        banner.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        banner.setActive(dto.getActive() != null ? dto.getActive() : true);
    }

    private BannerDTO toDTO(Banner banner) {
        BannerDTO dto = new BannerDTO();
        dto.setId(banner.getId());
        dto.setTitle(banner.getTitle());
        dto.setSubtitle(banner.getSubtitle());
        dto.setButtonText(banner.getButtonText());
        dto.setButtonLink(banner.getButtonLink());
        dto.setImageUrl(banner.getImageUrl());
        dto.setSortOrder(banner.getSortOrder());
        dto.setActive(banner.getActive());
        dto.setCreatedAt(banner.getCreatedAt());
        dto.setUpdatedAt(banner.getUpdatedAt());
        return dto;
    }
}
