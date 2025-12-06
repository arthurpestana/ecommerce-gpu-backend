package org.acme.dtos.user;

import java.util.List;

import org.acme.dtos.address.AddressResponseDTO;
import org.acme.models.User;
import org.acme.models.enums.UserRoles;


public record UserResponseDTO(
    String id,
    String name,
    String email,
    String phoneNumber,
    String cpf,
    UserRoles role,
    Boolean isActive,
    List<AddressResponseDTO> addresses
) {
    public static UserResponseDTO valueOf(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getCpf(),
            user.getRole(),
            user.getIsActive(),
            user.getAddresses() != null
                ? user.getAddresses().stream()
                    .map(AddressResponseDTO::valueOf)
                    .toList()
                : List.of()
        );
    }
}
