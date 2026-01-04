package com.tree.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.indexing.v3.Indexing;
import com.google.api.services.indexing.v3.model.PublishUrlNotificationResponse;
import com.google.api.services.indexing.v3.model.UrlNotification;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * Google Indexing API Service
 * Notify Google when articles are published/updated/deleted
 *
 * Setup:
 * 1. Go to Google Cloud Console
 * 2. Create a Service Account
 * 3. Enable Indexing API
 * 4. Download JSON key file
 * 5. Add service account email to Search Console as owner
 */
@Slf4j
@Service
public class GoogleIndexingService {

    private static final String INDEXING_SCOPE = "https://www.googleapis.com/auth/indexing";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Value("${google.indexing.enabled:false}")
    private boolean enabled;

    @Value("${google.credentials.file:}")
    private String credentialsFile;

    @Value("${google.credentials.json:}")
    private String credentialsJson;

    @Value("${app.site-url:http://localhost:3000}")
    private String siteUrl;

    private Indexing indexingService;

    @PostConstruct
    public void init() {
        if (!enabled) {
            log.info("Google Indexing API is disabled");
            return;
        }

        try {
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            GoogleCredentials credentials = loadCredentials();

            if (credentials == null) {
                log.warn("Google Indexing API: No credentials configured");
                enabled = false;
                return;
            }

            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
                    credentials.createScoped(Collections.singletonList(INDEXING_SCOPE))
            );

            indexingService = new Indexing.Builder(httpTransport, JSON_FACTORY, requestInitializer)
                    .setApplicationName("Tree Website")
                    .build();

            log.info("Google Indexing API initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize Google Indexing API: {}", e.getMessage());
            enabled = false;
        }
    }

    private GoogleCredentials loadCredentials() {
        try {
            // Try JSON content first (for cloud deployments)
            if (credentialsJson != null && !credentialsJson.isBlank()) {
                InputStream stream = new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8));
                return ServiceAccountCredentials.fromStream(stream);
            }

            // Try file path
            if (credentialsFile != null && !credentialsFile.isBlank()) {
                return ServiceAccountCredentials.fromStream(new FileInputStream(credentialsFile));
            }

            return null;
        } catch (Exception e) {
            log.error("Failed to load Google credentials: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Notify Google that a URL has been updated (new or modified content)
     */
    @Async
    public void notifyUrlUpdated(String path) {
        notifyUrl(path, "URL_UPDATED");
    }

    /**
     * Notify Google that a URL has been deleted
     */
    @Async
    public void notifyUrlDeleted(String path) {
        notifyUrl(path, "URL_DELETED");
    }

    /**
     * Notify Google about article publish/update
     */
    @Async
    public void notifyArticlePublished(String slug) {
        String path = "/blog/" + slug;
        notifyUrlUpdated(path);
    }

    /**
     * Notify Google about article deletion
     */
    @Async
    public void notifyArticleDeleted(String slug) {
        String path = "/blog/" + slug;
        notifyUrlDeleted(path);
    }

    /**
     * Notify Google about product publish/update
     */
    @Async
    public void notifyProductPublished(String slug) {
        String path = "/product/" + slug;
        notifyUrlUpdated(path);
    }

    /**
     * Notify Google about product deletion
     */
    @Async
    public void notifyProductDeleted(String slug) {
        String path = "/product/" + slug;
        notifyUrlDeleted(path);
    }

    private void notifyUrl(String path, String type) {
        if (!enabled || indexingService == null) {
            log.debug("Google Indexing API is disabled, skipping notification for: {}", path);
            return;
        }

        String url = siteUrl + path;

        try {
            UrlNotification notification = new UrlNotification()
                    .setUrl(url)
                    .setType(type);

            PublishUrlNotificationResponse response = indexingService.urlNotifications()
                    .publish(notification)
                    .execute();

            log.info("Google Indexing API: {} - {} - Notified at: {}",
                    type, url, response.getUrlNotificationMetadata().getLatestUpdate().getNotifyTime());
        } catch (Exception e) {
            log.error("Google Indexing API error for {}: {}", url, e.getMessage());
        }
    }

    public boolean isEnabled() {
        return enabled && indexingService != null;
    }
}
