package org.acme.dtos.manufacturer;

import java.util.UUID;

import org.acme.models.Manufacturer;

public record ManufacturerResponseDTO(
    UUID id,
    String name,
    String email,
    String cpnj,
    String country
) {
    public static ManufacturerResponseDTO valueOf(Manufacturer manufacturer) {
        if (manufacturer == null) return null;

        return new ManufacturerResponseDTO(
            manufacturer.getId(),
            manufacturer.getName(),
            manufacturer.getEmail(),
            manufacturer.getCpnj(),
            manufacturer.getCountry()
        );
    }
}
