package org.acme.dtos.auth;

import org.acme.dtos.user.UserResponseDTO;
import org.acme.models.User;

public record AuthResponseDTO(
    String token,
    UserResponseDTO user
) {
    public static AuthResponseDTO valueOf(String token, User user) {
        return new AuthResponseDTO(
            token,
            UserResponseDTO.valueOf(user)
        );
    }
}
