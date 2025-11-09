package org.acme.repositories;

import java.time.LocalDateTime;
import java.util.List;


import org.acme.models.InventoryTransaction;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InventoryTransactionRepository implements PanacheRepositoryBase<InventoryTransaction, String> {

    public PanacheQuery<InventoryTransaction> findAllTransactions() {
        return findAll();
    }

    public PanacheQuery<InventoryTransaction> findByGpuId(String gpuId) {
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

    public Long countAll() {
        return count();
    }
}
