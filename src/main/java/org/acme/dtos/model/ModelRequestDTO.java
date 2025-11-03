package org.acme.dtos.model;

import jakarta.validation.constraints.*;

public record ModelRequestDTO(

    @NotBlank(message = "O nome do modelo é obrigatório")
    @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres")
    String name,

    @NotNull(message = "O ano de lançamento é obrigatório")
    @Min(value = 1990, message = "O ano de lançamento deve ser maior que 1990")
    @Max(value = 2100, message = "O ano de lançamento deve ser válido")
    Integer releaseYear,

    @NotNull(message = "O ID do fabricante é obrigatório")
    Long manufacturerId
) {}
