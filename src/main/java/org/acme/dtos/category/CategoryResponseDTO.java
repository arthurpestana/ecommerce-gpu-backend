package org.acme.dtos.category;

import java.util.UUID;

import org.acme.models.Category;

public record CategoryResponseDTO(
    UUID id,
    String name,
    String description
) {
    public static CategoryResponseDTO valueOf(Category category) {
        if (category == null) {
            return null;
        }

        return new CategoryResponseDTO(
            category.getId(),
            category.getName(),
            category.getDescription()
        );
    }
}
