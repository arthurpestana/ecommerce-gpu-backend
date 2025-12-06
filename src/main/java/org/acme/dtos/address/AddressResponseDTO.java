package org.acme.dtos.address;



import org.acme.models.Address;

public record AddressResponseDTO(
    String id,
    String street,
    String city,
    String state,
    String zipCode,
    String country,
    String userId
) {
    public static AddressResponseDTO valueOf(Address address) {
        if (address == null) return null;

        return new AddressResponseDTO(
            address.getId(),
            address.getStreet(),
            address.getCity(),
            address.getState(),
            address.getZipCode(),
            address.getCountry(),
            address.getUser() != null ? address.getUser().getId() : null
        );
    }
}
