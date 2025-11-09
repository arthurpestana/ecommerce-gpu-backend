package org.acme.dtos.manufacturer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ManufacturerRequestDTO(

    @NotBlank(message = "O nome do fabricante é obrigatório")
    @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres")
    String name,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail informado não é válido")
    @Size(max = 150, message = "O e-mail pode ter no máximo 150 caracteres")
    String email,

    @NotBlank(message = "O CNPJ é obrigatório")
    @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter exatamente 14 dígitos numéricos")
    String cnpj,

    @NotBlank(message = "O país é obrigatório")
    @Size(max = 60, message = "O país pode ter no máximo 60 caracteres")
    String country
) {}
