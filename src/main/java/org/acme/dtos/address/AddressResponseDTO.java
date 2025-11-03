package org.acme.dtos.address;

import org.acme.dtos.user.UserResponseDTO;
import org.acme.models.Address;

public record AddressResponseDTO(
    Long id,
    String street,
    String city,
    String state,
    String zipCode,
    String country,
    UserResponseDTO user
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
            UserResponseDTO.valueOf(address.getUser())
        );
    }
}
