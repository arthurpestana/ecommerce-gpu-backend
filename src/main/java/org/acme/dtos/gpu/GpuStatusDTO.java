package org.acme.dtos.gpu;

import jakarta.validation.constraints.NotNull;

public record GpuStatusDTO(
  @NotNull(message = "O status 'isActive' não pode ser nulo")
  Boolean isActive
) {
}
