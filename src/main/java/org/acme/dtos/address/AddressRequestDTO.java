package org.acme.dtos.address;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRequestDTO(

    @NotBlank(message = "A rua é obrigatória")
    @Size(max = 150, message = "A rua pode ter no máximo 150 caracteres")
    String street,

    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 100, message = "A cidade pode ter no máximo 100 caracteres")
    String city,

    @NotBlank(message = "O estado é obrigatório")
    @Size(max = 100, message = "O estado pode ter no máximo 100 caracteres")
    String state,

    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "O CEP deve ter o formato 99999-999")
    String zipCode,

    @NotBlank(message = "O país é obrigatório")
    @Size(max = 100, message = "O país pode ter no máximo 100 caracteres")
    String country,

    @NotNull(message = "O ID do usuário é obrigatório")
    UUID userId
) {}
