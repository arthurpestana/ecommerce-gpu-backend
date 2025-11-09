package org.acme.dtos.model;

import java.util.UUID;

import org.acme.dtos.manufacturer.ManufacturerResponseDTO;
import org.acme.models.Model;

public record ModelResponseDTO(
    UUID id,
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
