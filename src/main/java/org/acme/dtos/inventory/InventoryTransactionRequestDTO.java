package org.acme.dtos.inventory;

import java.time.LocalDateTime;


import org.acme.models.enums.TransactionTypes;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InventoryTransactionRequestDTO(

    @NotNull(message = "O ID da GPU é obrigatório")
    String gpuId,

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    Integer quantity,

    @NotNull(message = "O tipo de transação é obrigatório")
    TransactionTypes transactionType,

    @Size(max = 255, message = "O motivo pode ter no máximo 255 caracteres")
    String reason,

    LocalDateTime transactionDate
) {}
