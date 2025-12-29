package com.tree.service;

import com.tree.dto.seo.SeoSettingsRequest;
import com.tree.dto.seo.SeoSettingsResponse;
import com.tree.entity.SeoSettings;
import com.tree.repository.SeoSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        SeoSettings settings = seoSettingsRepository.getSettings();

        if (settings == null) {
            settings = new SeoSettings();
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

        settings = seoSettingsRepository.save(settings);
        return SeoSettingsResponse.from(settings);
    }
}
