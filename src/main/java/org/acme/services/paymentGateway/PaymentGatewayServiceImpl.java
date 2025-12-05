package org.acme.services.paymentGateway;

import java.util.List;

import org.acme.dtos.payment.PaymentGatewayResponseDTO;
import org.acme.models.Payment;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class PaymentGatewayServiceImpl implements PaymentGatewayService {

    @ConfigProperty(name = "mp.access-token")
    String accessToken;

    @ConfigProperty(name = "mp.integrator-id")
    String integratorId;

    private PreferenceClient preferenceClient;

    private PaymentClient paymentClient = new PaymentClient();

    @PostConstruct
    void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
        MercadoPagoConfig.setIntegratorId(integratorId);

        this.preferenceClient = new PreferenceClient();
    }

    @Override
    public PaymentGatewayResponseDTO createCheckoutPayment(Payment payment) {
        try {
            // Payer (comprador)
            PreferencePayerRequest payer = PreferencePayerRequest.builder()
                    .email(payment.getOrder().getUser().getEmail())
                    .name(payment.getOrder().getUser().getName())
                    .build();

            // Item da compra
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title("Compra de GPU")
                    .quantity(1)
                    .unitPrice(payment.getAmount())
                    .build();

            // Preferência (Checkout Pro)
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(List.of(item))
                    .payer(payer)
                    .externalReference(payment.getId()) // usado no webhook depois
                    .notificationUrl("https://SEU_BACKEND/webhook/mercadopago") // ajuste aqui
                    .build();

            Preference preference = preferenceClient.create(preferenceRequest);

            return new PaymentGatewayResponseDTO(
                    preference.getId(), // gatewayPaymentId
                    "pending", // status inicial
                    preference.getInitPoint() // checkoutUrl
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar checkout do Mercado Pago", e);
        }
    }

    @Override
    public com.mercadopago.resources.payment.Payment getPaymentDetails(String paymentId) {
        try {
            return paymentClient.get(Long.parseLong(paymentId));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar pagamento no Mercado Pago", e);
        }
    }
}
