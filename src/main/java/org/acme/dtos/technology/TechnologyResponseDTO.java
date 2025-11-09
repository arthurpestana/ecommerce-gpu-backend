package org.acme.dtos.technology;

import java.util.UUID;

import org.acme.models.Technology;

public record TechnologyResponseDTO(
    UUID id,
    String name,
    String description
) {
    public static TechnologyResponseDTO valueOf(Technology tech) {
        if (tech == null) return null;
        return new TechnologyResponseDTO(tech.getId(), tech.getName(), tech.getDescription());
    }
}
