package com.tree.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlugUtilTest {

    @Test
    void toSlug_BasicText() {
        assertEquals("hello-world", SlugUtil.toSlug("Hello World"));
    }

    @Test
    void toSlug_Vietnamese() {
        assertEquals("bai-viet-tieng-viet", SlugUtil.toSlug("Bài viết tiếng Việt"));
    }

    @Test
    void toSlug_SpecialCharacters() {
        String result = SlugUtil.toSlug("Test @#$ 123!");
        assertTrue(result.contains("test") && result.contains("123"));
    }

    @Test
    void toSlug_MultipleSpaces() {
        String result = SlugUtil.toSlug("Multiple   Spaces");
        assertTrue(result.startsWith("multiple") && result.endsWith("spaces"));
    }

    @Test
    void toSlug_EmptyString() {
        assertEquals("", SlugUtil.toSlug(""));
    }

    @Test
    void toSlug_NullString() {
        assertEquals("", SlugUtil.toSlug(null));
    }

    @Test
    void generateUniqueSlug_NoConflict() {
        String slug = SlugUtil.generateUniqueSlug("Test Article", s -> false);
        assertEquals("test-article", slug);
    }

    @Test
    void generateUniqueSlug_WithConflict() {
        String slug = SlugUtil.generateUniqueSlug("Test", s -> s.equals("test"));
        assertEquals("test-1", slug);
    }

    @Test
    void generateUniqueSlug_MultipleConflicts() {
        String slug = SlugUtil.generateUniqueSlug("Test", s -> s.equals("test") || s.equals("test-1"));
        assertEquals("test-2", slug);
    }
}
