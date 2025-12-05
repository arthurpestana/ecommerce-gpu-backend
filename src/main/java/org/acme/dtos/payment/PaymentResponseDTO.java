package org.acme.dtos.payment;

import java.math.BigDecimal;

import org.acme.models.Payment;
import org.acme.models.enums.PaymentMethod;
import org.acme.models.enums.PaymentStatus;

public record PaymentResponseDTO(
    String id,
    String gatewayPaymentId,
    PaymentStatus paymentStatus,
    PaymentMethod paymentMethod,
    BigDecimal amount
) {
    public static PaymentResponseDTO valueOf(Payment payment) {
        if (payment == null) return null;

        return new PaymentResponseDTO(
            payment.getId(),
            payment.getGatewayPaymentId(),
            payment.getPaymentStatus(),
            payment.getPaymentMethod(),
            payment.getAmount()
        );
    }
}
