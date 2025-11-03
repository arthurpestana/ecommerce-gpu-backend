package org.acme.dtos.user;

import jakarta.validation.constraints.*;

public record UserRequestDTO(

    @NotBlank(message = "O nome do usuário é obrigatório")
    @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres")
    String name,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail informado não é válido")
    @Size(max = 150, message = "O e-mail pode ter no máximo 150 caracteres")
    String email,

    @NotBlank(message = "O número de telefone é obrigatório")
    @Pattern(regexp = "\\d{10,15}", message = "O telefone deve conter entre 10 e 15 dígitos numéricos")
    String phoneNumber,

    @NotBlank(message = "O CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos")
    String cpf,

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 100, message = "A senha deve conter entre 6 e 100 caracteres")
    String password,

    @NotNull(message = "O papel do usuário é obrigatório")
    String role,

    @NotNull(message = "O campo 'ativo' é obrigatório")
    Boolean isActive
) {}
