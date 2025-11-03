package org.acme.dtos.shared.pagination;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PaginationRequestDTO(

    @NotNull(message = "O offset é obrigatório")
    @PositiveOrZero(message = "O offset deve ser maior ou igual a 0")
    Integer offset,

    @NotNull(message = "O limit é obrigatório")
    @Min(value = 1, message = "O limit deve ser no mínimo 1")
    @Max(value = 100, message = "O limit não pode exceder 100 registros por página")
    Integer limit
) {
    
}
