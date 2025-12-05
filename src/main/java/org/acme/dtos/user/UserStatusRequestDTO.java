package org.acme.dtos.user;

import jakarta.validation.constraints.NotNull;

public record UserStatusRequestDTO(
  @NotNull(message = "O status 'isActive' não pode ser nulo")
  Boolean isActive
) {
}
