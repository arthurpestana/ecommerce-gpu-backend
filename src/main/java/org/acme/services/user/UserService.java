package org.acme.services.user;

import java.util.Optional;
import java.util.UUID;

import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.dtos.user.UserRequestDTO;
import org.acme.dtos.user.UserResponseDTO;

public interface UserService {

    PaginationResponseDTO<UserResponseDTO> findUserByName(String name, PaginationRequestDTO pagination);
    Optional<UserResponseDTO> findUserById(UUID id);
    PaginationResponseDTO<UserResponseDTO> findAllUsers(PaginationRequestDTO pagination);
    UserResponseDTO findUserByEmail(String email);
    UserResponseDTO createUser(UserRequestDTO dto);
    UserResponseDTO updateUser(UUID id, UserRequestDTO dto);
    Integer deleteUser(UUID id);
}
