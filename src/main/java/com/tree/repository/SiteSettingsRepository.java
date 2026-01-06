package com.tree.repository;

import com.tree.entity.SiteSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SiteSettingsRepository extends JpaRepository<SiteSettings, UUID> {

    default SiteSettings getSettings() {
        return findAll().stream().findFirst().orElse(null);
    }
}
