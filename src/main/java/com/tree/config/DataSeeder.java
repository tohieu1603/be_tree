package com.tree.config;

import com.tree.entity.Article;
import com.tree.entity.Category;
import com.tree.entity.User;
import com.tree.repository.ArticleRepository;
import com.tree.repository.CategoryRepository;
import com.tree.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedUsers();
        seedCategories();
        seedArticles();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .email("admin@tree.com")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Admin User")
                    .role(User.Role.ADMIN)
                    .active(true)
                    .build();
            userRepository.save(admin);
            log.info("Seeded admin user: admin@tree.com / admin123");
        }
    }

    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(Category.builder()
                    .name("Technology").slug("technology")
                    .description("Tech news and tutorials").sortOrder(1).active(true).build());
            categoryRepository.save(Category.builder()
                    .name("Products").slug("products")
                    .description("Product introductions").sortOrder(2).active(true).build());
            categoryRepository.save(Category.builder()
                    .name("News").slug("news")
                    .description("Latest news and updates").sortOrder(3).active(true).build());
            log.info("Seeded 3 categories");
        }
    }

    private void seedArticles() {
        if (articleRepository.count() == 0) {
            User admin = userRepository.findByEmail("admin@tree.com").orElse(null);
            Category tech = categoryRepository.findBySlug("technology").orElse(null);
            Category products = categoryRepository.findBySlug("products").orElse(null);

            if (admin != null && tech != null) {
                articleRepository.save(Article.builder()
                        .title("Welcome to Tree")
                        .slug("welcome-to-tree")
                        .summary("Introduction to our platform")
                        .content("# Welcome to Tree\n\nThis is our first article.\n\n## Features\n\n- Admin Panel\n- Markdown Editor\n- SEO Optimized")
                        .contentHtml("<h1>Welcome to Tree</h1><p>This is our first article.</p>")
                        .status(Article.Status.PUBLISHED)
                        .viewCount(10L)
                        .category(tech)
                        .author(admin)
                        .build());
            }

            if (admin != null && products != null) {
                articleRepository.save(Article.builder()
                        .title("New Product Launch 2024")
                        .slug("new-product-launch-2024")
                        .summary("Announcing our newest product line")
                        .content("# New Product Launch\n\nWe are excited to announce our latest product!\n\n## Key Features\n\n- Modern design\n- High performance")
                        .contentHtml("<h1>New Product Launch</h1><p>We are excited!</p>")
                        .status(Article.Status.PUBLISHED)
                        .viewCount(25L)
                        .category(products)
                        .author(admin)
                        .build());
            }
            log.info("Seeded 2 articles");
        }
    }
}
