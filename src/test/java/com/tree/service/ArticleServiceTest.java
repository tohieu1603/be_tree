package com.tree.service;

import com.tree.dto.article.ArticleRequest;
import com.tree.dto.article.ArticleResponse;
import com.tree.entity.Article;
import com.tree.entity.Category;
import com.tree.entity.User;
import com.tree.exception.ResourceNotFoundException;
import com.tree.repository.ArticleRepository;
import com.tree.repository.CategoryRepository;
import com.tree.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        articleRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        testUser = userRepository.save(User.builder()
                .email("test@example.com")
                .password(passwordEncoder.encode("password"))
                .fullName("Test User")
                .role(User.Role.ADMIN)
                .active(true)
                .build());

        testCategory = categoryRepository.save(Category.builder()
                .name("Test Category")
                .slug("test-category")
                .active(true)
                .sortOrder(1)
                .build());
    }

    @Test
    void createArticle_Success() {
        ArticleRequest request = new ArticleRequest();
        request.setTitle("Test Article");
        request.setContent("# Hello World\n\nThis is content.");
        request.setCategoryId(testCategory.getId().toString());

        ArticleResponse response = articleService.create(request, testUser.getId());

        assertNotNull(response.getId());
        assertEquals("Test Article", response.getTitle());
        assertEquals("test-article", response.getSlug());
        assertNotNull(response.getContentHtml());
        assertTrue(response.getContentHtml().contains("<h1>"));
    }

    @Test
    void createArticle_AutoGeneratesSlug() {
        ArticleRequest request = new ArticleRequest();
        request.setTitle("Bài Viết Tiếng Việt");
        request.setContent("Content here");

        ArticleResponse response = articleService.create(request, testUser.getId());

        assertEquals("bai-viet-tieng-viet", response.getSlug());
    }

    @Test
    void getBySlug_IncrementsViewCount() {
        Article article = articleRepository.save(Article.builder()
                .title("Test").slug("test").content("Content")
                .status(Article.Status.PUBLISHED).viewCount(0L)
                .author(testUser).build());

        ArticleResponse response = articleService.getBySlug("test");

        assertEquals(1L, response.getViewCount());
    }

    @Test
    void getBySlug_NotFound_ThrowsException() {
        assertThrows(ResourceNotFoundException.class, () -> {
            articleService.getBySlug("non-existent");
        });
    }

    @Test
    void updateArticle_Success() {
        Article article = articleRepository.save(Article.builder()
                .title("Original").slug("original").content("Original content")
                .status(Article.Status.DRAFT).viewCount(0L)
                .author(testUser).build());

        ArticleRequest request = new ArticleRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated content");
        request.setStatus("PUBLISHED");

        ArticleResponse response = articleService.update(article.getId(), request);

        assertEquals("Updated Title", response.getTitle());
        assertEquals("PUBLISHED", response.getStatus());
    }

    @Test
    void deleteArticle_Success() {
        Article article = articleRepository.save(Article.builder()
                .title("To Delete").slug("to-delete").content("Content")
                .status(Article.Status.DRAFT).viewCount(0L)
                .author(testUser).build());

        articleService.delete(article.getId());

        assertFalse(articleRepository.existsById(article.getId()));
    }

    @Test
    void convertHtmlToMarkdown_Success() {
        String html = "<h1>Title</h1><p>Paragraph</p><ul><li>Item 1</li><li>Item 2</li></ul>";
        String markdown = articleService.convertHtmlToMarkdown(html);

        assertNotNull(markdown);
        assertTrue(markdown.contains("Title"));
        assertTrue(markdown.contains("Paragraph"));
    }
}
