package org.acme.repositories;

import org.acme.models.InventoryTransaction;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class InventoryTransactionRepository implements PanacheRepository<InventoryTransaction> {

    public PanacheQuery<InventoryTransaction> findAllTransactions() {
        return findAll();
    }

    public PanacheQuery<InventoryTransaction> findByGpuId(Long gpuId) {
        return find("gpu.id", gpuId);
    }

    public PanacheQuery<InventoryTransaction> findByTransactionType(String type) {
        return find("transactionType", type);
    }

    public PanacheQuery<InventoryTransaction> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return find("transactionDate BETWEEN ?1 AND ?2", start, end);
    }

    public List<InventoryTransaction> findRecentTransactions(int limit) {
        return find("ORDER BY transactionDate DESC").page(0, limit).list();
    }

    public long countAll() {
        return count();
    }
}
