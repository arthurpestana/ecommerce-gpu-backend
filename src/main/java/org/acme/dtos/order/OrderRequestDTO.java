package org.acme.dtos.order;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.acme.models.enums.PaymentMethod;

public record OrderRequestDTO(

    @NotNull(message = "O ID do usuário é obrigatório")
    String userId,

    @NotNull(message = "O endereço é obrigatório")
    String addressId,

    @Size(min = 1, message = "O pedido deve conter ao menos um item")
    List<OrderItemRequestDTO> items,

    @NotNull(message = "O método de pagamento é obrigatório")
    PaymentMethod paymentMethod

) {}
