package org.acme.resources;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import org.acme.services.payment.PaymentService;

@Path("/webhook/payment-gateway")
@RequestScoped
public class PaymentGatewayWebhookResource {
    @Inject
    PaymentService paymentService;

    @POST
    public Response receiveWebhook(String body) {
        try {
            paymentService.handlePayment(body);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(500).entity("Erro no processamento do webhook").build();
        }
    }
}
