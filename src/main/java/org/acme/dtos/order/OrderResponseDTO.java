package org.acme.dtos.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.acme.dtos.address.AddressResponseDTO;
import org.acme.dtos.payment.PaymentResponseDTO;
import org.acme.dtos.user.UserResponseDTO;
import org.acme.models.Order;

public record OrderResponseDTO(
        String id,
        LocalDateTime orderDate,
        BigDecimal totalAmount,
        String orderStatus,
        List<OrderItemResponseDTO> items,
        PaymentResponseDTO payment,
        UserResponseDTO user,
        AddressResponseDTO address,
        String checkoutUrl) {

    public static OrderResponseDTO valueOf(Order order) {
        if (order == null)
            return null;
        return valueOf(order, null);
    }

    public static OrderResponseDTO valueOf(Order order, String checkoutUrl) {
        if (order == null)
            return null;

        return new OrderResponseDTO(
                order.getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getOrderStatus().name(),

                order.getItems() != null
                        ? order.getItems().stream().map(OrderItemResponseDTO::valueOf).toList()
                        : List.of(),

                PaymentResponseDTO.valueOf(order.getPayment()),

                UserResponseDTO.valueOf(order.getUser()),
                AddressResponseDTO.valueOf(order.getAddress()),
                checkoutUrl);
    }
}
