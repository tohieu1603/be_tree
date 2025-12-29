package com.tree.repository;

import com.tree.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findBySlug(String slug);
    boolean existsBySlug(String slug);
    List<Category> findByActiveOrderBySortOrderAsc(boolean active);
    List<Category> findByActiveTrueOrderBySortOrderAsc();

    // Tree structure queries
    List<Category> findByParentIsNullOrderBySortOrderAsc();
    List<Category> findByParentIsNullAndActiveTrueOrderBySortOrderAsc();
    List<Category> findByParentIdOrderBySortOrderAsc(UUID parentId);
}
