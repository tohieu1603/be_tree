package com.tree.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IndexNow API Service
 * Instantly notify search engines when content is published/updated
 *
 * Supported search engines: Bing, Yandex, Seznam.cz, Naver
 *
 * Setup:
 * 1. Generate a key (8-128 hex characters)
 * 2. Create file at /[key].txt containing the key
 * 3. Set INDEXNOW_KEY env variable
 */
@Slf4j
@Service
public class IndexNowService {

    private static final String INDEXNOW_API = "https://api.indexnow.org/indexnow";

    @Value("${indexnow.enabled:false}")
    private boolean enabled;

    @Value("${indexnow.key:}")
    private String apiKey;

    @Value("${app.site-url:http://localhost:3000}")
    private String siteUrl;

    private final RestTemplate restTemplate;

    public IndexNowService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Notify IndexNow about article publish/update
     */
    @Async
    public void notifyArticlePublished(String slug) {
        String path = "/blog/" + slug;
        notifyUrl(path);
    }

    /**
     * Notify IndexNow about product publish/update
     */
    @Async
    public void notifyProductPublished(String slug) {
        String path = "/product/" + slug;
        notifyUrl(path);
    }

    /**
     * Notify IndexNow about a single URL update
     */
    @Async
    public void notifyUrl(String path) {
        if (!enabled || apiKey == null || apiKey.isBlank()) {
            log.debug("IndexNow is disabled or no API key configured, skipping notification for: {}", path);
            return;
        }

        String fullUrl = siteUrl + path;

        try {
            String host = extractHost(siteUrl);
            String keyLocation = siteUrl + "/" + apiKey + ".txt";

            Map<String, Object> payload = new HashMap<>();
            payload.put("host", host);
            payload.put("key", apiKey);
            payload.put("keyLocation", keyLocation);
            payload.put("urlList", List.of(fullUrl));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(INDEXNOW_API, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("IndexNow: Successfully notified - {}", fullUrl);
            } else {
                log.warn("IndexNow: Notification returned status {} for {}", response.getStatusCode(), fullUrl);
            }
        } catch (Exception e) {
            log.error("IndexNow: Failed to notify for {}: {}", fullUrl, e.getMessage());
        }
    }

    /**
     * Notify IndexNow about multiple URLs at once (batch)
     */
    @Async
    public void notifyUrls(List<String> paths) {
        if (!enabled || apiKey == null || apiKey.isBlank()) {
            log.debug("IndexNow is disabled or no API key configured");
            return;
        }

        if (paths == null || paths.isEmpty()) {
            return;
        }

        try {
            String host = extractHost(siteUrl);
            String keyLocation = siteUrl + "/" + apiKey + ".txt";

            List<String> fullUrls = paths.stream()
                    .map(path -> siteUrl + path)
                    .toList();

            Map<String, Object> payload = new HashMap<>();
            payload.put("host", host);
            payload.put("key", apiKey);
            payload.put("keyLocation", keyLocation);
            payload.put("urlList", fullUrls);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(INDEXNOW_API, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("IndexNow: Successfully notified {} URLs", fullUrls.size());
            } else {
                log.warn("IndexNow: Batch notification returned status {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("IndexNow: Failed batch notification: {}", e.getMessage());
        }
    }

    private String extractHost(String url) {
        try {
            java.net.URI uri = java.net.URI.create(url);
            return uri.getHost();
        } catch (Exception e) {
            // Fallback: remove protocol
            return url.replaceFirst("https?://", "").split("/")[0];
        }
    }

    public boolean isEnabled() {
        return enabled && apiKey != null && !apiKey.isBlank();
    }
}
