package org.acme.dtos.category;

import org.acme.models.Category;

public record CategoryResponseDTO(
    Long id,
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
