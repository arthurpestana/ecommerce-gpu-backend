package org.acme.services.order;

import java.util.Optional;

import org.acme.dtos.order.OrderRequestDTO;
import org.acme.dtos.order.OrderResponseDTO;
import org.acme.dtos.shared.pagination.PaginationRequestDTO;
import org.acme.dtos.shared.pagination.PaginationResponseDTO;

public interface OrderService {

    PaginationResponseDTO<OrderResponseDTO> findAllOrders(PaginationRequestDTO pagination);
    Optional<OrderResponseDTO> findOrderById(String id);

    PaginationResponseDTO<OrderResponseDTO> findByUser(String userId, PaginationRequestDTO pagination);

    OrderResponseDTO createOrder(OrderRequestDTO dto);
    void updateOrderStatus(String orderId, String paymentStatus);
}
