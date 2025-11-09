package org.acme.utils;

import java.util.Locale;
import java.util.UUID;

public final class FileNaming {
    private FileNaming() {}

    public static String extensionFromContentType(String contentType, String fallback) {
        if (contentType == null) return fallback;
        return switch (contentType.toLowerCase(Locale.ROOT)) {
            case "image/jpeg", "image/jpg" -> "jpg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            case "image/gif" -> "gif";
            default -> fallback;
        };
    }

    public static String randomName(String prefix, String ext) {
        String safeExt = (ext == null || ext.isBlank()) ? "bin" : ext;
        return "%s-%s.%s".formatted(prefix, UUID.randomUUID(), safeExt);
    }
}
