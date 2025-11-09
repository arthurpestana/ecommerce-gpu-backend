package org.acme.dtos.image;

import jakarta.validation.constraints.NotNull;

import java.util.List;


public record ImageDeleteManyRequestDTO(
    @NotNull(message = "A lista de IDs de imagens não pode ser nula")
    List<String> imageIds
) {}
