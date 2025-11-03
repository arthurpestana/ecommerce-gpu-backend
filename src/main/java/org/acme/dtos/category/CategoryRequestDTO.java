package org.acme.dtos.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequestDTO(

    @NotBlank(message = "O nome da categoria é obrigatório")
    @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres")
    String name,

    @Size(max = 255, message = "A descrição pode ter no máximo 255 caracteres")
    String description
) {}
