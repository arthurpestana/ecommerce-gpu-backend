package org.acme.dtos.user;

import org.acme.models.User;
import org.acme.models.enums.UserRoles;

public record UserResponseDTO(
    Long id,
    String name,
    String email,
    String phoneNumber,
    String cpf,
    UserRoles role,
    Boolean isActive
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
            user.getIsActive()
        );
    }
}
