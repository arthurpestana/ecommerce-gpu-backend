package org.acme.dtos.technology;



import org.acme.models.Technology;

public record TechnologyResponseDTO(
    String id,
    String name,
    String description
) {
    public static TechnologyResponseDTO valueOf(Technology tech) {
        if (tech == null) return null;
        return new TechnologyResponseDTO(tech.getId(), tech.getName(), tech.getDescription());
    }
}
