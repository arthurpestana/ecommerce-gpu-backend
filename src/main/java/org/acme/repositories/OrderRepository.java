package org.acme.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.acme.models.Order;
import org.acme.models.enums.OrderStatus;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderRepository implements PanacheRepositoryBase<Order, String> {

    public Optional<Order> findOrderById(String id) {
        return findByIdOptional(id);
    }

    public PanacheQuery<Order> findAllOrders() {
        return findAll();
    }

    public PanacheQuery<Order> findByUserId(String userId) {
        return find("user.id = ?1", userId);
    }

    public PanacheQuery<Order> findByStatus(OrderStatus status) {
        return find("orderStatus = ?1", status);
    }

    public PanacheQuery<Order> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return find("orderDate BETWEEN ?1 AND ?2", start, end);
    }

    public PanacheQuery<Order> findByAddress(String addressId) {
        return find("address.id = ?1", addressId);
    }

    public Long countAllOrders() {
        return count();
    }

    public PanacheQuery<Order> findFiltered(
        String userId,
        OrderStatus status,
        LocalDateTime startDate,
        LocalDateTime endDate
    ) {
        StringBuilder query = new StringBuilder("1=1");
        var params = new java.util.ArrayList<>();

        if (userId != null) {
            query.append(" AND user.id = ?").append(params.size() + 1);
            params.add(userId);
        }

        if (status != null) {
            query.append(" AND orderStatus = ?").append(params.size() + 1);
            params.add(status);
        }

        if (startDate != null) {
            query.append(" AND orderDate >= ?").append(params.size() + 1);
            params.add(startDate);
        }

        if (endDate != null) {
            query.append(" AND orderDate <= ?").append(params.size() + 1);
            params.add(endDate);
        }

        return find(query.toString(), params.toArray());
    }
}
