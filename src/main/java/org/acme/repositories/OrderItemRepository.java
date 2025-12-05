package org.acme.repositories;

import java.util.List;
import java.util.Optional;

import org.acme.models.OrderItem;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderItemRepository implements PanacheRepositoryBase<OrderItem, String> {

    public Optional<OrderItem> findItemById(String id) {
        return findByIdOptional(id);
    }

    public PanacheQuery<OrderItem> findAllItems() {
        return findAll();
    }

    public List<OrderItem> findByOrderId(String orderId) {
        return list("order.id = ?1", orderId);
    }

    public PanacheQuery<OrderItem> findByGpuId(String gpuId) {
        return find("gpu.id = ?1", gpuId);
    }

    public Long countItemsByOrder(String orderId) {
        return count("order.id = ?1", orderId);
    }
}
