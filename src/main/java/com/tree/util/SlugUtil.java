package com.tree.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class SlugUtil {
    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGES_DASHES = Pattern.compile("(^-|-$)");

    private SlugUtil() {}

    public static String toSlug(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String withoutAccents = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String replaced = WHITESPACE.matcher(withoutAccents).replaceAll("-");
        String slug = NON_LATIN.matcher(replaced).replaceAll("");
        slug = EDGES_DASHES.matcher(slug).replaceAll("");

        return slug.toLowerCase(Locale.ENGLISH);
    }

    public static String generateUniqueSlug(String baseSlug, java.util.function.Predicate<String> existsCheck) {
        String slug = toSlug(baseSlug);
        if (!existsCheck.test(slug)) {
            return slug;
        }

        int counter = 1;
        String newSlug;
        do {
            newSlug = slug + "-" + counter++;
        } while (existsCheck.test(newSlug));

        return newSlug;
    }
}
