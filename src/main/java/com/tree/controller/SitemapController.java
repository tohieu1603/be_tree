package com.tree.controller;

import com.tree.entity.Article;
import com.tree.entity.Category;
import com.tree.repository.ArticleRepository;
import com.tree.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Sitemap", description = "Sitemap generation")
public class SitemapController {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    @Value("${app.site-url:http://localhost:3000}")
    private String siteUrl;

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "Generate sitemap.xml")
    public ResponseEntity<String> getSitemap() {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        // Homepage
        xml.append("  <url>\n");
        xml.append("    <loc>").append(siteUrl).append("/</loc>\n");
        xml.append("    <changefreq>daily</changefreq>\n");
        xml.append("    <priority>1.0</priority>\n");
        xml.append("  </url>\n");

        // Articles page
        xml.append("  <url>\n");
        xml.append("    <loc>").append(siteUrl).append("/articles</loc>\n");
        xml.append("    <changefreq>daily</changefreq>\n");
        xml.append("    <priority>0.9</priority>\n");
        xml.append("  </url>\n");

        // Published articles
        List<Article> articles = articleRepository.findByStatusOrderByCreatedAtDesc(Article.Status.PUBLISHED);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

        for (Article article : articles) {
            xml.append("  <url>\n");
            xml.append("    <loc>").append(siteUrl).append("/article/").append(escapeXml(article.getSlug())).append("</loc>\n");
            if (article.getUpdatedAt() != null) {
                xml.append("    <lastmod>").append(article.getUpdatedAt().toLocalDate().format(formatter)).append("</lastmod>\n");
            }
            xml.append("    <changefreq>weekly</changefreq>\n");
            xml.append("    <priority>0.8</priority>\n");
            xml.append("  </url>\n");
        }

        // Active categories
        List<Category> categories = categoryRepository.findByActiveTrueOrderBySortOrderAsc();
        for (Category category : categories) {
            xml.append("  <url>\n");
            xml.append("    <loc>").append(siteUrl).append("/category/").append(escapeXml(category.getSlug())).append("</loc>\n");
            xml.append("    <changefreq>weekly</changefreq>\n");
            xml.append("    <priority>0.7</priority>\n");
            xml.append("  </url>\n");
        }

        xml.append("</urlset>");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(xml.toString());
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Generate robots.txt")
    public ResponseEntity<String> getRobots() {
        String robots = "User-agent: *\nAllow: /\n\nSitemap: " + siteUrl + "/sitemap.xml\n";

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(robots);
    }

    private String escapeXml(String value) {
        if (value == null) return "";
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
