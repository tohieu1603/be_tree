package com.tree.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tree.dto.SiteSettingsDTO;
import com.tree.entity.SiteSettings;
import com.tree.repository.SiteSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SiteSettingsService {

    private final SiteSettingsRepository siteSettingsRepository;
    private final ObjectMapper objectMapper;

    public SiteSettingsDTO getSettings() {
        SiteSettings settings = siteSettingsRepository.getSettings();
        if (settings == null) {
            return getDefaultSettings();
        }
        return toDTO(settings);
    }

    @Transactional
    public SiteSettingsDTO saveSettings(SiteSettingsDTO dto) {
        SiteSettings settings = siteSettingsRepository.getSettings();
        if (settings == null) {
            settings = new SiteSettings();
        }
        updateFromDTO(settings, dto);
        return toDTO(siteSettingsRepository.save(settings));
    }

    private SiteSettingsDTO getDefaultSettings() {
        SiteSettingsDTO dto = new SiteSettingsDTO();
        dto.setSiteName("Duc Viet");
        dto.setSiteUrl("https://tramducviet.com");
        dto.setSiteTagline("Tinh Hoa Tram Huong");
        dto.setSiteDescription("Chuyen cung cap vong tay tram huong, tuong phat, nhang tram, tinh dau tram huong thien nhien 100%");
        dto.setHeroTitle("TRAM HUONG");
        dto.setHeroSubtitle("Tinh Hoa Thien Nhien Viet Nam");
        dto.setCategorySectionTitle("Tinh Hoa Tram Huong");
        dto.setCategorySectionSubtitle("Curated By Duc Viet");
        dto.setServiceSectionTitle("DUC VIET SERVICES");
        dto.setCopyrightText("2024 Duc Viet. All rights reserved.");

        // Default services
        List<SiteSettingsDTO.ServiceItem> services = new ArrayList<>();
        services.add(new SiteSettingsDTO.ServiceItem(
            "GIAO HANG TAN NOI",
            "Mien phi giao hang toan quoc cho don hang tu 2 trieu dong.",
            "/uploads/products/vong-tay-tram-huong-2.jpg",
            "Tim Hieu Them",
            "/about"
        ));
        services.add(new SiteSettingsDTO.ServiceItem(
            "TU VAN CHUYEN GIA",
            "Doi ngu chuyen gia tram huong tu van mien phi.",
            "/uploads/products/tuong-phat-tram-huong-1.jpg",
            "Lien He Ngay",
            "/contact"
        ));
        services.add(new SiteSettingsDTO.ServiceItem(
            "BAO HANH TRON DOI",
            "Cam ket bao hanh tron doi cho tat ca san pham tram huong.",
            "/uploads/products/nhang-tram-huong.jpg",
            "Chinh Sach Bao Hanh",
            "/warranty"
        ));
        dto.setServices(services);

        // Default navigation menu
        List<SiteSettingsDTO.MenuItem> leftMenu = new ArrayList<>();
        leftMenu.add(new SiteSettingsDTO.MenuItem("Bộ Sưu Tập", "/products"));
        leftMenu.add(new SiteSettingsDTO.MenuItem("Câu Chuyện", "/articles"));
        dto.setNavLeftMenu(leftMenu);

        List<SiteSettingsDTO.MenuItem> rightMenu = new ArrayList<>();
        rightMenu.add(new SiteSettingsDTO.MenuItem("Về Chúng Tôi", "/about"));
        rightMenu.add(new SiteSettingsDTO.MenuItem("Liên Hệ", "/contact"));
        dto.setNavRightMenu(rightMenu);

        // Default theme settings
        dto.setFontPreset("elegant");
        dto.setColorPalette("warmGold");
        dto.setBorderRadius("subtle");

        return dto;
    }

    private void updateFromDTO(SiteSettings settings, SiteSettingsDTO dto) {
        settings.setSiteName(dto.getSiteName());
        settings.setSiteUrl(dto.getSiteUrl());
        settings.setSiteTagline(dto.getSiteTagline());
        settings.setSiteDescription(dto.getSiteDescription());
        settings.setLogoUrl(dto.getLogoUrl());
        settings.setLogoDarkUrl(dto.getLogoDarkUrl());
        settings.setFaviconUrl(dto.getFaviconUrl());
        settings.setContactEmail(dto.getContactEmail());
        settings.setContactPhone(dto.getContactPhone());
        settings.setContactAddress(dto.getContactAddress());
        settings.setFacebookUrl(dto.getFacebookUrl());
        settings.setInstagramUrl(dto.getInstagramUrl());
        settings.setYoutubeUrl(dto.getYoutubeUrl());
        settings.setTiktokUrl(dto.getTiktokUrl());
        settings.setZaloUrl(dto.getZaloUrl());
        settings.setFooterText(dto.getFooterText());
        settings.setCopyrightText(dto.getCopyrightText());
        settings.setHeroTitle(dto.getHeroTitle());
        settings.setHeroSubtitle(dto.getHeroSubtitle());
        settings.setCategorySectionTitle(dto.getCategorySectionTitle());
        settings.setCategorySectionSubtitle(dto.getCategorySectionSubtitle());
        settings.setServiceSectionTitle(dto.getServiceSectionTitle());

        // Convert services to JSON
        if (dto.getServices() != null) {
            try {
                settings.setServicesJson(objectMapper.writeValueAsString(dto.getServices()));
            } catch (JsonProcessingException e) {
                log.error("Error serializing services", e);
            }
        }

        // Convert navigation menu to JSON
        if (dto.getNavLeftMenu() != null) {
            try {
                settings.setNavLeftMenuJson(objectMapper.writeValueAsString(dto.getNavLeftMenu()));
            } catch (JsonProcessingException e) {
                log.error("Error serializing navLeftMenu", e);
            }
        }
        if (dto.getNavRightMenu() != null) {
            try {
                settings.setNavRightMenuJson(objectMapper.writeValueAsString(dto.getNavRightMenu()));
            } catch (JsonProcessingException e) {
                log.error("Error serializing navRightMenu", e);
            }
        }

        // Theme settings
        settings.setFontPreset(dto.getFontPreset() != null ? dto.getFontPreset() : "elegant");
        settings.setColorPalette(dto.getColorPalette() != null ? dto.getColorPalette() : "warmGold");
        settings.setBorderRadius(dto.getBorderRadius() != null ? dto.getBorderRadius() : "subtle");
    }

    private SiteSettingsDTO toDTO(SiteSettings settings) {
        SiteSettingsDTO dto = new SiteSettingsDTO();
        dto.setSiteName(settings.getSiteName());
        dto.setSiteUrl(settings.getSiteUrl());
        dto.setSiteTagline(settings.getSiteTagline());
        dto.setSiteDescription(settings.getSiteDescription());
        dto.setLogoUrl(settings.getLogoUrl());
        dto.setLogoDarkUrl(settings.getLogoDarkUrl());
        dto.setFaviconUrl(settings.getFaviconUrl());
        dto.setContactEmail(settings.getContactEmail());
        dto.setContactPhone(settings.getContactPhone());
        dto.setContactAddress(settings.getContactAddress());
        dto.setFacebookUrl(settings.getFacebookUrl());
        dto.setInstagramUrl(settings.getInstagramUrl());
        dto.setYoutubeUrl(settings.getYoutubeUrl());
        dto.setTiktokUrl(settings.getTiktokUrl());
        dto.setZaloUrl(settings.getZaloUrl());
        dto.setFooterText(settings.getFooterText());
        dto.setCopyrightText(settings.getCopyrightText());
        dto.setHeroTitle(settings.getHeroTitle());
        dto.setHeroSubtitle(settings.getHeroSubtitle());
        dto.setCategorySectionTitle(settings.getCategorySectionTitle());
        dto.setCategorySectionSubtitle(settings.getCategorySectionSubtitle());
        dto.setServiceSectionTitle(settings.getServiceSectionTitle());

        // Parse services JSON
        if (settings.getServicesJson() != null && !settings.getServicesJson().isEmpty()) {
            try {
                List<SiteSettingsDTO.ServiceItem> services = objectMapper.readValue(
                    settings.getServicesJson(),
                    new TypeReference<List<SiteSettingsDTO.ServiceItem>>() {}
                );
                dto.setServices(services);
            } catch (JsonProcessingException e) {
                log.error("Error parsing services JSON", e);
                dto.setServices(new ArrayList<>());
            }
        } else {
            dto.setServices(new ArrayList<>());
        }

        // Parse navigation menu JSON
        if (settings.getNavLeftMenuJson() != null && !settings.getNavLeftMenuJson().isEmpty()) {
            try {
                List<SiteSettingsDTO.MenuItem> navLeftMenu = objectMapper.readValue(
                    settings.getNavLeftMenuJson(),
                    new TypeReference<List<SiteSettingsDTO.MenuItem>>() {}
                );
                dto.setNavLeftMenu(navLeftMenu);
            } catch (JsonProcessingException e) {
                log.error("Error parsing navLeftMenu JSON", e);
                dto.setNavLeftMenu(new ArrayList<>());
            }
        } else {
            dto.setNavLeftMenu(new ArrayList<>());
        }

        if (settings.getNavRightMenuJson() != null && !settings.getNavRightMenuJson().isEmpty()) {
            try {
                List<SiteSettingsDTO.MenuItem> navRightMenu = objectMapper.readValue(
                    settings.getNavRightMenuJson(),
                    new TypeReference<List<SiteSettingsDTO.MenuItem>>() {}
                );
                dto.setNavRightMenu(navRightMenu);
            } catch (JsonProcessingException e) {
                log.error("Error parsing navRightMenu JSON", e);
                dto.setNavRightMenu(new ArrayList<>());
            }
        } else {
            dto.setNavRightMenu(new ArrayList<>());
        }

        // Theme settings
        dto.setFontPreset(settings.getFontPreset() != null ? settings.getFontPreset() : "elegant");
        dto.setColorPalette(settings.getColorPalette() != null ? settings.getColorPalette() : "warmGold");
        dto.setBorderRadius(settings.getBorderRadius() != null ? settings.getBorderRadius() : "subtle");

        return dto;
    }
}
