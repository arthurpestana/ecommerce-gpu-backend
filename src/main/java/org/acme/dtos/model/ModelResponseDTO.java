package org.acme.dtos.model;

import org.acme.models.Model;
import org.acme.dtos.manufacturer.ManufacturerResponseDTO;

public record ModelResponseDTO(
    Long id,
    String name,
    Integer releaseYear,
    ManufacturerResponseDTO manufacturer
) {
    public static ModelResponseDTO valueOf(Model model) {
        if (model == null) return null;

        return new ModelResponseDTO(
            model.getId(),
            model.getName(),
            model.getReleaseYear(),
            ManufacturerResponseDTO.valueOf(model.getManufacturer())
        );
    }
}
