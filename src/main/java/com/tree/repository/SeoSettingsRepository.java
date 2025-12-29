package com.tree.repository;

import com.tree.entity.SeoSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeoSettingsRepository extends JpaRepository<SeoSettings, UUID> {

    default SeoSettings getSettings() {
        return findAll().stream().findFirst().orElse(null);
    }
}
