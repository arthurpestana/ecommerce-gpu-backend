package org.acme.dtos.technology;

import jakarta.validation.constraints.*;

public record TechnologyRequestDTO(
    @NotBlank(message = "O nome da tecnologia é obrigatório")
    @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres")
    String name,

    @Size(max = 255, message = "A descrição pode ter no máximo 255 caracteres")
    String description
) {}
