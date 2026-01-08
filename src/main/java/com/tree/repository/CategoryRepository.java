package com.tree.repository;

import com.tree.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    // Native delete to bypass JPA cascade
    @Modifying
    @Query(value = "DELETE FROM categories WHERE id = :id", nativeQuery = true)
    void deleteByIdNative(@Param("id") UUID id);
    Optional<Category> findBySlug(String slug);
    Optional<Category> findBySlugAndDeletedFalse(String slug);
    boolean existsBySlug(String slug);

    // Non-deleted queries
    List<Category> findByDeletedFalseOrderBySortOrderAsc();
    List<Category> findByActiveAndDeletedFalseOrderBySortOrderAsc(boolean active);
    List<Category> findByActiveTrueAndDeletedFalseOrderBySortOrderAsc();

    // Trash queries
    List<Category> findByDeletedTrueOrderByDeletedAtDesc();

    // Tree structure queries (non-deleted)
    List<Category> findByParentIsNullAndDeletedFalseOrderBySortOrderAsc();
    List<Category> findByParentIsNullAndActiveTrueAndDeletedFalseOrderBySortOrderAsc();
    List<Category> findByParentIdAndDeletedFalseOrderBySortOrderAsc(UUID parentId);

    // Legacy methods
    List<Category> findByActiveOrderBySortOrderAsc(boolean active);
    List<Category> findByActiveTrueOrderBySortOrderAsc();
    List<Category> findByParentIsNullOrderBySortOrderAsc();
    List<Category> findByParentIsNullAndActiveTrueOrderBySortOrderAsc();
    List<Category> findByParentIdOrderBySortOrderAsc(UUID parentId);
}
