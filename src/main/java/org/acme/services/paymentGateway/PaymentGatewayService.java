package org.acme.services.paymentGateway;

import org.acme.dtos.payment.PaymentGatewayResponseDTO;
import org.acme.models.Payment;

public interface PaymentGatewayService {
    PaymentGatewayResponseDTO createCheckoutPayment(Payment payment);
    com.mercadopago.resources.payment.Payment getPaymentDetails(String paymentId);
}
