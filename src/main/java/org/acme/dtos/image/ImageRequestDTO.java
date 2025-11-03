package org.acme.dtos.image;

import jakarta.validation.constraints.*;

public record ImageRequestDTO(
    @NotBlank(message = "A URL da imagem é obrigatória")
    @Size(max = 255, message = "A URL pode ter no máximo 255 caracteres")
    String url,

    @Size(max = 150, message = "O texto alternativo pode ter no máximo 150 caracteres")
    String altText
) {}
