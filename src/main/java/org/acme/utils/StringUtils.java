package org.acme.utils;

public final class StringUtils {

    private StringUtils() {}

    public static String safeTrim(String s) {
        return s == null ? null : s.trim();
    }

    public static String normalizeDigits(String s) {
        return (s == null) ? null : s.replaceAll("\\D", "");
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isAlphanumeric(String s) {
        return s != null && s.matches("[A-Za-z0-9]+");
    }

    public static String truncateString(String s, int maxLength) {
        if (s == null || s.length() <= maxLength) return s;
        return s.substring(0, Math.max(0, maxLength - 3)) + "...";
    }
}
