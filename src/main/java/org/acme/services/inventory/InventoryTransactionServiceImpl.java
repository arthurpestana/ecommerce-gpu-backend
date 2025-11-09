package org.acme.services.inventory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


import org.acme.dtos.inventory.InventoryTransactionRequestDTO;
import org.acme.dtos.inventory.InventoryTransactionResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;
import org.acme.models.Gpu;
import org.acme.models.InventoryTransaction;
import org.acme.models.enums.TransactionTypes;
import org.acme.repositories.GpuRepository;
import org.acme.repositories.InventoryTransactionRepository;
import org.acme.utils.StringUtils;
import org.acme.utils.ValidationUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class InventoryTransactionServiceImpl implements InventoryTransactionService {

    @Inject
    InventoryTransactionRepository transactionRepository;

    @Inject
    GpuRepository gpuRepository;

    @Inject
    Validator validator;

    @Override
    public PaginationResponseDTO<InventoryTransactionResponseDTO> findAllTransactions(PaginationRequestDTO pagination) {
        List<InventoryTransaction> transactions = transactionRepository.findAllTransactions()
                .page(pagination.page(), pagination.limit())
                .list();

        Long total = transactionRepository.countAll();

        List<InventoryTransactionResponseDTO> list = transactions.stream()
                .map(InventoryTransactionResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(list, pagination.page(), pagination.limit(), total);
    }

    @Override
    public PaginationResponseDTO<InventoryTransactionResponseDTO> findByGpu(String gpuId, PaginationRequestDTO pagination) {
        List<InventoryTransaction> transactions = transactionRepository.findByGpuId(gpuId)
                .page(pagination.page(), pagination.limit())
                .list();

        Long total = transactionRepository.findByGpuId(gpuId).count();

        List<InventoryTransactionResponseDTO> list = transactions.stream()
                .map(InventoryTransactionResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(list, pagination.page(), pagination.limit(), total);
    }

    @Override
    public PaginationResponseDTO<InventoryTransactionResponseDTO> findByTransactionType(String type, PaginationRequestDTO pagination) {
        List<InventoryTransaction> transactions = transactionRepository.findByTransactionType(type)
                .page(pagination.page(), pagination.limit())
                .list();

        Long total = transactionRepository.findByTransactionType(type).count();

        List<InventoryTransactionResponseDTO> list = transactions.stream()
                .map(InventoryTransactionResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(list, pagination.page(), pagination.limit(), total);
    }

    @Override
    public PaginationResponseDTO<InventoryTransactionResponseDTO> findByDateRange(LocalDateTime start, LocalDateTime end, PaginationRequestDTO pagination) {
        List<InventoryTransaction> transactions = transactionRepository.findByDateRange(start, end)
                .page(pagination.page(), pagination.limit())
                .list();

        Long total = transactionRepository.findByDateRange(start, end).count();

        List<InventoryTransactionResponseDTO> list = transactions.stream()
                .map(InventoryTransactionResponseDTO::valueOf)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(list, pagination.page(), pagination.limit(), total);
    }

    @Override
    public Optional<InventoryTransactionResponseDTO> findById(String id) {
        return transactionRepository.findByIdOptional(id).map(InventoryTransactionResponseDTO::valueOf);
    }

    @Override
    @Transactional
    public InventoryTransactionResponseDTO createTransaction(InventoryTransactionRequestDTO dto) {
        ValidationUtils.validateDto(validator, dto);

        Gpu gpu = gpuRepository.findByIdOptional(dto.gpuId())
                .orElseThrow(() -> new NotFoundException("GPU não encontrada com o ID informado."));

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setGpu(gpu);
        transaction.setQuantity(dto.quantity());
        transaction.setReason(StringUtils.safeTrim(dto.reason()));
        transaction.setTransactionDate(dto.transactionDate() != null ? dto.transactionDate() : LocalDateTime.now());
        transaction.setTransactionType(dto.transactionType());

        Integer currentStock = gpu.getAvailableQuantity();
        if (dto.transactionType() == TransactionTypes.ADD) {
            gpu.setAvailableQuantity(currentStock + dto.quantity());
        } else if (dto.transactionType() == TransactionTypes.REMOVE) {
            if (currentStock < dto.quantity()) {
                throw new IllegalArgumentException("Quantidade insuficiente em estoque para realizar a remoção.");
            }

            gpu.setAvailableQuantity(Math.max(0, currentStock - dto.quantity()));
        }

        transactionRepository.persist(transaction);
        return InventoryTransactionResponseDTO.valueOf(transaction);
    }

    @Override
    @Transactional
    public Integer deleteTransaction(String id) {
        return transactionRepository.deleteById(id) ? 1 : 0;
    }
}
