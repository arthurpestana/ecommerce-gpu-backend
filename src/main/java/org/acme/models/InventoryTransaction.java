package org.acme.models;

import jakarta.persistence.*;
import java.time.*;

import org.acme.models.enums.TransactionTypes;

@Entity
@Table(name = "inventory_transactions")
public class InventoryTransaction extends DefaultEntity {

    @ManyToOne
    @JoinColumn(name = "gpu_id", nullable = false)
    private Gpu gpu;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "reason", length = 255, nullable = true)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionTypes transactionType;

    public Gpu getGpu() {
        return gpu;
    }

    public void setGpu(Gpu gpu) {
        this.gpu = gpu;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public TransactionTypes getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypes transactionType) {
        this.transactionType = transactionType;
    }

}
