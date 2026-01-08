package com.tree.repository;

import com.tree.entity.Article;
import com.tree.entity.Article.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {
    Optional<Article> findBySlug(String slug);
    Optional<Article> findBySlugAndDeletedFalse(String slug);
    boolean existsBySlug(String slug);

    // Non-deleted queries
    Page<Article> findByDeletedFalse(Pageable pageable);
    Page<Article> findByStatusAndDeletedFalse(Status status, Pageable pageable);
    Page<Article> findByCategoryIdAndStatusAndDeletedFalse(UUID categoryId, Status status, Pageable pageable);

    // Trash queries
    Page<Article> findByDeletedTrue(Pageable pageable);

    // Count non-deleted articles by category
    long countByCategoryIdAndDeletedFalse(UUID categoryId);

    // Legacy methods
    Page<Article> findByStatus(Status status, Pageable pageable);
    Page<Article> findByCategoryIdAndStatus(UUID categoryId, Status status, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE a.status = :status AND a.deleted = false ORDER BY a.createdAt DESC")
    Page<Article> findPublishedArticles(@Param("status") Status status, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE " +
           "(LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND a.status = :status AND a.deleted = false")
    Page<Article> searchArticles(@Param("keyword") String keyword, @Param("status") Status status, Pageable pageable);

    List<Article> findByStatusAndDeletedFalseOrderByCreatedAtDesc(Status status);
    List<Article> findByStatusOrderByCreatedAtDesc(Status status);
}
