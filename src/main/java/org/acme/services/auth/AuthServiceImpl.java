package org.acme.services.auth;

import org.acme.dtos.auth.AuthResponseDTO;
import org.acme.dtos.auth.LoginRequestDTO;
import org.acme.dtos.auth.RegisterRequestDTO;
import org.acme.dtos.user.UserResponseDTO;
import org.acme.models.User;
import org.acme.models.enums.UserRoles;
import org.acme.repositories.UserRepository;
import org.acme.utils.HashPassword;
import org.acme.utils.JwtConfig;
import org.acme.utils.StringUtils;
import org.acme.utils.ValidationUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

    @Inject
    UserRepository userRepository;

    @Inject
    HashPassword hashPassword;

    @Inject
    JwtConfig jwtConfig;

    @Inject
    Validator validator;

    @Override
    @Transactional
    public AuthResponseDTO login(LoginRequestDTO dto) {
        ValidationUtils.validateDto(validator, dto);

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new NotFoundException("Credenciais inválidas."));

        String savedPassword = user.getPassword();
        String rawPassword = dto.password();

        Boolean isHashed = hashPassword.isHash(savedPassword);

        Boolean validPassword;

        if (isHashed) {
            String encryptedPassword = hashPassword.getHashPassword(rawPassword);
            validPassword = encryptedPassword.equals(savedPassword);
        } else {
            validPassword = rawPassword.equals(savedPassword);
        }

        if (!validPassword) {
            throw new BadRequestException("Credenciais inválidas.");
        }

        if (!user.getIsActive()) {
            throw new BadRequestException("Usuário está desativado.");
        }

        String token = jwtConfig.generateJwt(user);

        return new AuthResponseDTO(
                token,
                UserResponseDTO.valueOf(user));
    }

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO dto) {
        ValidationUtils.validateDto(validator, dto);

        if (userRepository.existsByEmail(dto.email()))
            throw new BadRequestException("E-mail já está em uso.");

        // if (userRepository.existsByCpf(dto.cpf()))
        // throw new BadRequestException("CPF já está cadastrado.");

        // if (userRepository.existsByPhoneNumber(dto.phoneNumber()))
        // throw new BadRequestException("Telefone já está em uso.");

        User user = new User();
        user.setName(StringUtils.safeTrim(dto.name()));
        user.setEmail(StringUtils.safeTrim(dto.email()));
        user.setPhoneNumber(StringUtils.normalizeDigits(dto.phoneNumber()));
        user.setCpf(StringUtils.normalizeDigits(dto.cpf()));
        user.setPassword(hashPassword.getHashPassword(dto.password()));
        user.setRole(UserRoles.CUSTOMER);
        user.setIsActive(true);

        userRepository.persist(user);

        String token = jwtConfig.generateJwt(user);

        return new AuthResponseDTO(
                token,
                UserResponseDTO.valueOf(user));
    }
}
