package org.acme.dtos.payment;

public record PaymentGatewayResponseDTO(
    String gatewayPaymentId,
    String status,
    String checkoutUrl
) {}
