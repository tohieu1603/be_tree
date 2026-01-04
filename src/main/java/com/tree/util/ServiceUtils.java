package com.tree.util;

import com.tree.exception.BadRequestException;
import com.tree.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Utility class for common service operations.
 */
@Slf4j
public final class ServiceUtils {

    private ServiceUtils() {}

    /**
     * Execute operation with try-catch and logging
     */
    public static <T> T execute(String operation, Supplier<T> supplier) {
        try {
            log.debug("Executing: {}", operation);
            T result = supplier.get();
            log.debug("Completed: {}", operation);
            return result;
        } catch (Exception e) {
            log.error("Failed: {} - {}", operation, e.getMessage());
            throw e;
        }
    }

    /**
     * Execute void operation with try-catch and logging
     */
    public static void executeVoid(String operation, Runnable runnable) {
        try {
            log.debug("Executing: {}", operation);
            runnable.run();
            log.debug("Completed: {}", operation);
        } catch (Exception e) {
            log.error("Failed: {} - {}", operation, e.getMessage());
            throw e;
        }
    }

    /**
     * Find entity or throw ResourceNotFoundException
     */
    public static <T> T findOrThrow(Optional<T> optional, String entityName, String field, Object value) {
        return optional.orElseThrow(() -> new ResourceNotFoundException(entityName, field, value));
    }

    /**
     * Find entity by ID or throw
     */
    public static <T> T findByIdOrThrow(Function<UUID, Optional<T>> finder, UUID id, String entityName) {
        return finder.apply(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName, "id", id));
    }

    /**
     * Validate slug uniqueness, generate if needed
     */
    public static String validateOrGenerateSlug(String slug, String name, Predicate<String> existsCheck) {
        if (slug == null || slug.isBlank()) {
            return SlugUtil.generateUniqueSlug(name, existsCheck);
        }
        if (existsCheck.test(slug)) {
            throw new BadRequestException("Slug already exists: " + slug);
        }
        return slug;
    }

    /**
     * Validate slug uniqueness on update (excluding current entity)
     */
    public static void validateSlugOnUpdate(String newSlug, String currentSlug, Predicate<String> existsCheck) {
        if (newSlug != null && !newSlug.equals(currentSlug) && existsCheck.test(newSlug)) {
            throw new BadRequestException("Slug already exists: " + newSlug);
        }
    }

    /**
     * Parse UUID from string, throw BadRequest if invalid
     */
    public static UUID parseUUID(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid " + fieldName + ": " + value);
        }
    }

    /**
     * Get value or default
     */
    public static <T> T getOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * Get value or default from supplier (lazy evaluation)
     */
    public static <T> T getOrDefault(T value, Supplier<T> defaultSupplier) {
        return value != null ? value : defaultSupplier.get();
    }

    /**
     * Require non-null value
     */
    public static <T> T requireNonNull(T value, String fieldName) {
        if (value == null) {
            throw new BadRequestException(fieldName + " is required");
        }
        return value;
    }

    /**
     * Require non-blank string
     */
    public static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException(fieldName + " is required");
        }
        return value;
    }

    /**
     * Update field if new value is not null
     */
    public static <T> void updateIfNotNull(T newValue, java.util.function.Consumer<T> setter) {
        if (newValue != null) {
            setter.accept(newValue);
        }
    }

    /**
     * Update string field if not blank
     */
    public static void updateIfNotBlank(String newValue, java.util.function.Consumer<String> setter) {
        if (newValue != null && !newValue.isBlank()) {
            setter.accept(newValue);
        }
    }
}
