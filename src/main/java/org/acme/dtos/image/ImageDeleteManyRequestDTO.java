package org.acme.dtos.image;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ImageDeleteManyRequestDTO(
    @NotNull(message = "A lista de IDs de imagens não pode ser nula")
    List<UUID> imageIds
) {}
