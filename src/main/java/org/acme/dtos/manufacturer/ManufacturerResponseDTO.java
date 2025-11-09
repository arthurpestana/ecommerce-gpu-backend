package org.acme.dtos.manufacturer;



import org.acme.models.Manufacturer;

public record ManufacturerResponseDTO(
    String id,
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
