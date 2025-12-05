package org.acme.services.payment;

import org.acme.models.Payment;
import org.acme.models.enums.PaymentStatus;
import org.acme.repositories.PaymentRepository;
import org.acme.services.paymentGateway.PaymentGatewayService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class PaymentServiceImpl implements PaymentService {

    @Inject
    PaymentRepository paymentRepository;

    @Inject
    PaymentGatewayService paymentGatewayService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    @Transactional
    public void handlePayment(String payload) {
        try {
            JsonNode json = mapper.readTree(payload);

            String eventType = json.get("type").asText();
            if (!"payment".equals(eventType)) {
                return;
            }

            String paymentId = json.get("data").get("id").asText();

            var mpPayment = paymentGatewayService.getPaymentDetails(paymentId);

            String externalReference = mpPayment.getExternalReference();
            String status = mpPayment.getStatus();

            Payment payment = paymentRepository.findByIdOptional(externalReference)
                    .orElseThrow(() -> new RuntimeException("Pagamento não encontrado no banco: " + externalReference));

            payment.setPaymentStatus(PaymentStatus.valueOf(status.toUpperCase()));

            paymentRepository.persist(payment);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar webhook", e);
        }
    }
}
