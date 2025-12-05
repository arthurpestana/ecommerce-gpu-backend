package org.acme.repositories;

import java.util.Optional;

import org.acme.models.Payment;
import org.acme.models.enums.PaymentMethod;
import org.acme.models.enums.PaymentStatus;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PaymentRepository implements PanacheRepositoryBase<Payment, String> {

    public Optional<Payment> findPaymentById(String id) {
        return findByIdOptional(id);
    }

    public Optional<Payment> findByGatewayPaymentId(String gatewayPaymentId) {
        return find("gatewayPaymentId = ?1", gatewayPaymentId).firstResultOptional();
    }

    public PanacheQuery<Payment> findAllPayments() {
        return findAll();
    }

    public PanacheQuery<Payment> findByOrderId(String orderId) {
        return find("order.id = ?1", orderId);
    }

    public PanacheQuery<Payment> findByStatus(PaymentStatus status) {
        return find("paymentStatus = ?1", status);
    }

    public PanacheQuery<Payment> findByMethod(PaymentMethod method) {
        return find("paymentMethod = ?1", method);
    }

    public Long countPaymentsByOrder(String orderId) {
        return count("order.id = ?1", orderId);
    }

    public PanacheQuery<Payment> findFiltered(
        String orderId,
        PaymentMethod method,
        PaymentStatus status
    ) {
        StringBuilder query = new StringBuilder("1=1");
        var params = new java.util.ArrayList<>();

        if (orderId != null) {
            query.append(" AND order.id = ?").append(params.size() + 1);
            params.add(orderId);
        }

        if (method != null) {
            query.append(" AND paymentMethod = ?").append(params.size() + 1);
            params.add(method);
        }

        if (status != null) {
            query.append(" AND paymentStatus = ?").append(params.size() + 1);
            params.add(status);
        }

        return find(query.toString(), params.toArray());
    }
}
