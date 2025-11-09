package org.acme.services.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.dtos.user.UserRequestDTO;
import org.acme.dtos.user.UserResponseDTO;
import org.acme.models.User;
import org.acme.models.enums.UserRoles;
import org.acme.repositories.UserRepository;
import org.acme.utils.StringUtils;
import org.acme.utils.ValidationUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    Validator validator;

    @Override
    public Optional<UserResponseDTO> findUserById(String id) {
        return userRepository.findByIdOptional(id)
                .map(UserResponseDTO::valueOf);
    }

    @Override
    public PaginationResponseDTO<UserResponseDTO> findUserByName(String name, PaginationRequestDTO pagination) {
        List<User> users = userRepository.findByName(name)
                .page(pagination.page(), pagination.limit())
                .list();

        Long total = userRepository.findByName(name).count();

        List<UserResponseDTO> userResponseList = users.stream()
                .map(UserResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(
                userResponseList,
                pagination.page(),
                pagination.limit(),
                total
        );
    }

    @Override
    public PaginationResponseDTO<UserResponseDTO> findAllUsers(PaginationRequestDTO pagination) {
        List<User> users = userRepository.findAllUsers()
                .page(pagination.page(), pagination.limit())
                .list();

        Long total = userRepository.findAllUsers().count();

        List<UserResponseDTO> userResponseList = users.stream()
                .map(UserResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(
                userResponseList,
                pagination.page(),
                pagination.limit(),
                total
        );
    }

    @Override
    public UserResponseDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("Usuário não encontrado com o e-mail: " + email));

        return UserResponseDTO.valueOf(user);
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        ValidationUtils.validateDto(validator, dto);

        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("E-mail já está em uso.");
        }
        if (userRepository.existsByCpf(dto.cpf())) {
            throw new IllegalArgumentException("CPF já está cadastrado.");
        }
        if (userRepository.existsByPhoneNumber(dto.phoneNumber())) {
            throw new IllegalArgumentException("Telefone já está em uso.");
        }

        User user = new User();
        user.setName(StringUtils.safeTrim(dto.name()));
        user.setEmail(StringUtils.safeTrim(dto.email()));
        user.setPhoneNumber(StringUtils.normalizeDigits(dto.phoneNumber()));
        user.setCpf(StringUtils.normalizeDigits(dto.cpf()));
        user.setPassword(dto.password());
        user.setRole(UserRoles.valueOf(dto.role().toUpperCase()));
        user.setIsActive(dto.isActive());

        userRepository.persist(user);
        return UserResponseDTO.valueOf(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(String id, UserRequestDTO dto) {
        ValidationUtils.validateDto(validator, dto);
        if (id == null) {
            throw new IllegalArgumentException("ID do Usuário não pode ser nulo.");
        }

        User user = userRepository.findByIdOptional(id)
            .orElseThrow(() -> new NotFoundException("Usuário não encontrado com o ID: " + id));

        User updatedUser = user;

        updatedUser.setName(StringUtils.safeTrim(dto.name()));
        updatedUser.setEmail(StringUtils.safeTrim(dto.email()));
        updatedUser.setPhoneNumber(StringUtils.normalizeDigits(dto.phoneNumber()));
        updatedUser.setCpf(StringUtils.normalizeDigits(dto.cpf()));
        updatedUser.setPassword(dto.password());
        updatedUser.setRole(UserRoles.valueOf(dto.role().toUpperCase()));
        updatedUser.setIsActive(dto.isActive());

        userRepository.persist(updatedUser);
        return UserResponseDTO.valueOf(updatedUser);
    }

    @Override
    @Transactional
    public Integer deleteUser(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do Usuário não pode ser nulo.");
        }

        userRepository.findByIdOptional(id)
            .orElseThrow(() -> new NotFoundException("Usuário não encontrado com o ID: " + id));

        boolean deleted = userRepository.deleteById(id);
        return deleted ? 1 : 0;
    }
}
