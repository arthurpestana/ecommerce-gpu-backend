package org.acme.services.inventory;

import java.time.LocalDateTime;
import java.util.Optional;

import org.acme.dtos.inventory.InventoryTransactionRequestDTO;
import org.acme.dtos.inventory.InventoryTransactionResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;

public interface InventoryTransactionService {

    PaginationResponseDTO<InventoryTransactionResponseDTO> findAllTransactions(PaginationRequestDTO pagination);
    PaginationResponseDTO<InventoryTransactionResponseDTO> findByGpu(Long gpuId, PaginationRequestDTO pagination);
    PaginationResponseDTO<InventoryTransactionResponseDTO> findByTransactionType(String type, PaginationRequestDTO pagination);
    PaginationResponseDTO<InventoryTransactionResponseDTO> findByDateRange(LocalDateTime start, LocalDateTime end, PaginationRequestDTO pagination);

    Optional<InventoryTransactionResponseDTO> findById(Long id);

    InventoryTransactionResponseDTO createTransaction(InventoryTransactionRequestDTO dto);

    Integer deleteTransaction(Long id);
}
