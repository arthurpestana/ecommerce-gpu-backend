package org.acme.dtos.inventory;

import java.time.LocalDateTime;


import org.acme.dtos.gpu.GpuResponseDTO;
import org.acme.models.InventoryTransaction;
import org.acme.models.enums.TransactionTypes;

public record InventoryTransactionResponseDTO(
    String id,
    GpuResponseDTO gpu,
    Integer quantity,
    LocalDateTime transactionDate,
    String reason,
    TransactionTypes transactionType
) {
    public static InventoryTransactionResponseDTO valueOf(InventoryTransaction transaction) {
        if (transaction == null) return null;

        return new InventoryTransactionResponseDTO(
            transaction.getId(),
            GpuResponseDTO.valueOf(transaction.getGpu()),
            transaction.getQuantity(),
            transaction.getTransactionDate(),
            transaction.getReason(),
            transaction.getTransactionType()
        );
    }
}
