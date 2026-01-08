package com.tree.repository;

import com.tree.entity.Product;
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
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findBySlug(String slug);

    Optional<Product> findBySlugAndDeletedFalse(String slug);

    boolean existsBySlug(String slug);

    // Non-deleted products queries
    Page<Product> findByDeletedFalse(Pageable pageable);

    Page<Product> findByIsActiveTrueAndDeletedFalse(Pageable pageable);

    Page<Product> findByCategoryIdAndIsActiveTrueAndDeletedFalse(UUID categoryId, Pageable pageable);

    List<Product> findByIsFeaturedTrueAndIsActiveTrueAndDeletedFalse();

    // Trash (deleted products)
    Page<Product> findByDeletedTrue(Pageable pageable);

    // Find all products by category (for cascade operations)
    List<Product> findByCategoryId(UUID categoryId);

    // Legacy methods (keep for backward compatibility)
    Page<Product> findByIsActiveTrue(Pageable pageable);

    Page<Product> findByCategoryIdAndIsActiveTrue(UUID categoryId, Pageable pageable);

    List<Product> findByIsFeaturedTrueAndIsActiveTrue();

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.deleted = false AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.summary) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.id != :productId AND p.isActive = true AND p.deleted = false")
    List<Product> findRelatedProducts(@Param("categoryId") UUID categoryId, @Param("productId") UUID productId, Pageable pageable);
}
