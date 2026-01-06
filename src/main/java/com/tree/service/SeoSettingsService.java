package com.tree.service;

import com.tree.dto.seo.SeoSettingsRequest;
import com.tree.dto.seo.SeoSettingsResponse;
import com.tree.entity.SeoSettings;
import com.tree.repository.SeoSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeoSettingsService {

    private final SeoSettingsRepository seoSettingsRepository;

    public SeoSettingsResponse getSettings() {
        SeoSettings settings = seoSettingsRepository.getSettings();
        return SeoSettingsResponse.from(settings);
    }

    @Transactional
    public SeoSettingsResponse saveSettings(SeoSettingsRequest request) {
        log.info("Saving SEO settings: siteName={}, siteUrl={}", request.getSiteName(), request.getSiteUrl());

        SeoSettings settings = seoSettingsRepository.getSettings();

        if (settings == null) {
            log.info("Creating new SEO settings record");
            settings = SeoSettings.builder()
                    .robotsAllowAll(true)
                    .robotsDisallowPaths("/admin/,/api/")
                    .sitemapEnabled(true)
                    .sitemapIncludeArticles(true)
                    .sitemapIncludeCategories(true)
                    .sitemapChangeFrequency("weekly")
                    .build();
        }

        settings.setSiteName(request.getSiteName());
        settings.setSiteUrl(request.getSiteUrl());
        settings.setMetaTitle(request.getMetaTitle());
        settings.setMetaDescription(request.getMetaDescription());
        settings.setMetaKeywords(request.getMetaKeywords());

        settings.setRobotsAllowAll(request.isRobotsAllowAll());
        settings.setRobotsDisallowPaths(request.getRobotsDisallowPaths());
        settings.setRobotsCustomRules(request.getRobotsCustomRules());

        settings.setSitemapEnabled(request.isSitemapEnabled());
        settings.setSitemapIncludeArticles(request.isSitemapIncludeArticles());
        settings.setSitemapIncludeCategories(request.isSitemapIncludeCategories());
        settings.setSitemapChangeFrequency(request.getSitemapChangeFrequency());

        settings.setGoogleAnalyticsId(request.getGoogleAnalyticsId());
        settings.setGoogleVerification(request.getGoogleVerification());

        settings.setOgImage(request.getOgImage());
        settings.setTwitterHandle(request.getTwitterHandle());

        try {
            settings = seoSettingsRepository.save(settings);
            log.info("SEO settings saved successfully with id={}", settings.getId());
            return SeoSettingsResponse.from(settings);
        } catch (Exception e) {
            log.error("Error saving SEO settings", e);
            throw e;
        }
    }
}
